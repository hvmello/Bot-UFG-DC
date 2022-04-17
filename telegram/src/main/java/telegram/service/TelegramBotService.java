package telegram.service;

import comum.model.persistent.Alerta;
import comum.model.persistent.Telegram;
import comum.model.persistent.telegram.Location;
import comum.model.persistent.telegram.TradeMessage;
import comum.repository.AlertaRepository;
import comum.repository.LocationRepository;
import comum.repository.TelegramRepository;
import io.swagger.models.auth.In;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.auth.OidcService;
import telegram.config.BotProperties;
import telegram.utilitarios.Utilitarios;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Log4j2
public class TelegramBotService extends TelegramLongPollingBot {

    @Autowired
    UserService userService;

    @Autowired
    TelegramRepository telegramRepository;

    @Autowired
    LocationRepository locationRepository;

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotService.class);

//    private final String username;
//    private final String token;
//    private final OidcService oidcService;

//    public TelegramBotService(BotProperties properties, OidcService oidcService) {
//        super(properties.createOptions());
//        this.username = properties.getUsername();
//        this.token = properties.getToken();
//        this.oidcService = oidcService;
//    }


    @Autowired
    MessageService messageService;
    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @SneakyThrows
    @Transactional
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {  //Processamento de Mensagens de TEXTO

            var userId = update.getMessage().getFrom().getId();
            var chatId = update.getMessage().getChatId();
//            oidcService.findUserInfo(userId).ifPresentOrElse(
//                    userInfo -> {
            try {
                postInlineButton(chatId);
            } catch (IOException e) {
                e.printStackTrace();
            }
//                    },
////                    () -> askForLogin(userId, chatId));
        } else if (update.hasCallbackQuery()) {                      //Processamento de CallBACKs de Buttons
            messageService.processButtonResponseCommonUser(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasDocument()) {
            var userId = update.getMessage().getFrom().getId();
            var chatId = update.getMessage().getChatId();

//            oidcService.findUserInfo(userId).ifPresentOrElse(
//                    userInfo -> {
//                        if (userService.hasRole(userInfo.getPreferredUsername(), "admin")) {
            try {
                messageService.processFile(update.getMessage().getDocument());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//                        }
//                    },
//                    () -> askForLoginAdmin(userId, chatId));

        } else if(update.hasMessage() && update.getMessage().hasLocation()){
            messageService.processLocation(update.getMessage());
        }
    }


//    private void askForLogin(Integer userId, Long chatId) {
//        var url = oidcService.getAuthUrl(userId);
//        var message = String.format("Área reservada para usuários do Comando de Operações de Defesa Civil. \n" +
//                "Por favor, <a href=\"%s\">clique aqui</a> para fazer seu login.", url);
//        sendHtmlMessage(message, chatId);
//    }
//
//    private void askForLoginAdmin(Integer userId, Long chatId) {
//        var url = oidcService.getAuthUrl(userId);
//        var message = String.format("Somente Usuários Administradores do Sistema podem executar esta ação. \n" +
//                "Por favor, faça seu login com usuário admin <a href=\"%s\">clicando aqui</a>.", url);
//        sendHtmlMessage(message, chatId);
//    } TODO

    void sendHtmlMessage(String message, Long chatId) {
        var sendMessage = new SendMessage(chatId, message);
        sendMessage.setParseMode("HTML");
        tryExecute(sendMessage);
    }

    private void tryExecute(BotApiMethod<?> action) {
        try {
            execute(action);
        } catch (TelegramApiException e) {
            String message = String.format(
                    "Ooops! Something went wrong during action execution. Action: %s. Error: %s.",
                    action,
                    e);
            log.error(message, e);
        }
    }

    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }

    public void postMessage(List<TradeMessage> msgs) {
        for (TradeMessage msg : msgs) {
            postMessage(msg);
        }
    }

    public void postMessage(SendMessage sendResponse) {

        try {
            execute(sendResponse); //at the end, so some magic and send the message ;)
        } catch (TelegramApiException e) {
            System.out.println("Falha ao enviar ");//do some error handling
        }//end catch()
    }

    public void postMessage(EditMessageText sendResponse) {

        try {
            execute(sendResponse); //at the end, so some magic and send the message ;)
        } catch (TelegramApiException e) {
            System.out.println("Falha ao enviar ");//do some error handling
        }//end catch()
    }

    public void postMessage(TradeMessage msg) {

        //create a object that contains the information to send back the message
        SendMessage sendMessageRequest = new SendMessage();

        sendMessageRequest.enableHtml(true);
        sendMessageRequest.enableWebPagePreview();
        sendMessageRequest.setChatId(msg.getChatId()); //who should get the message? the sender from which we got the message...
        sendMessageRequest.setText(msg.getText());

        try {
            execute(sendMessageRequest); //at the end, so some magic and send the message ;)
        } catch (TelegramApiException e) {
            //do some error handling
        }//end catch()

    }//end enviarParaTodos()

    public void postImagesMessages(List<SendPhoto> messages) throws IOException {

        for (SendPhoto msg : messages) {
            postImageMessage(msg);
        }
    }


    //enviar mensagens com template
    public void postImageMessage(SendPhoto message) throws IOException {

        try {
            execute(message); //at the end, so some magic and send the message ;)
        } catch (TelegramApiException e) {
            //do some error handling
        }//end catch()

    }//end

    //enviar mensagens com template
    public void postInlineButton(Long chatId) throws IOException {

        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chatId);


        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();

        message.setText("Escolha uma das opções:");

        rowInline.add(new InlineKeyboardButton().setText("Consultar Últimos Alertas").setCallbackData("search_latest"));
        rowInline2.add(new InlineKeyboardButton().setText("Cadastrar Localização").setCallbackData("register_cep"));
        rowInline3.add(new InlineKeyboardButton().setText("Ativar/Desativar Alertas").setCallbackData("activate_alerts"));
        rowInline4.add(new InlineKeyboardButton().setText("Informar uma Ocorrência").setCallbackData("report_alert"));


        // Set the keyboard to the markup
        // rowsInline.add(rowInline);
        rowsInline.add(rowInline);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        rowsInline.add(rowInline4);

        // Add it to the message
        //Declara uma linha do tipo KeyBoard Markup
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Mensagem de botões não enviada");
        }

    }

//    public File executeFile(GetFile getFile){
//        try {
//            return execute(getFile); //at the end, so some magic and send the message ;)
//        } catch (TelegramApiException e) {
//            //do some error handling
//        }//end catch()
//        return null;
//    }

    public void createTextForMessageAndSendAllUsers(Alerta alerta) throws Exception{


        SendMessage message = new SendMessage(); // Create a message object object


        String messageAlertasConcat = alerta.getTituloAlerta() + " - " +
                Utilitarios.formatarData(alerta.getDataAlerta()) + " - " +
                alerta.getTextoAlerta() +
                "\n\n";

        message.setText(messageAlertasConcat);

        List<Location> listaTodasLocalizacoesCadastradas =
                locationRepository.findAllLocationsInsideRectangle(alerta.getLatitudeTop(), alerta.getLatitudeBottom(),
                        alerta.getLongitudeLeft(), alerta.getLongitudeRight());
        List<Long> listaTelegramChatIds = listaTodasLocalizacoesCadastradas.stream().map(item -> item.getTelegram().getTelegramId()).collect(Collectors.toList());


        int cont = 1;
        for (Long item : listaTelegramChatIds) {

            if(cont % 30 == 0){
                Thread.sleep(2 * 1000 * 60); // A cada 30 mensagens espera dois minutos para evitar ban por spam
            }

            message.setChatId(item);
            this.postMessage(message);

            cont++;
        }


    }
//Lógica para encontrar localizaçoes dentro da área para emitir o alerta
//    boolean isDentroAreaLocalizacao(double top, double bottom, double left, double right,
//                      double latitude, double longitude){
//
//        if(top >= latitude && latitude >= bottom){
//            if(left <= right && left <= longitude && longitude <= right){
//                return true;
//            } else if(left > right && (left <= longitude || longitude <= right)) {
//                return true;
//            }
//        }
//        return false;
//    }

}
