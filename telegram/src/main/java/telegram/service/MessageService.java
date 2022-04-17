package telegram.service;

import comum.model.dto.security.LoginFormTelegram;
import comum.model.enums.EnumTipoMensagem;
import comum.model.persistent.Alerta;
import comum.model.persistent.MensagemPadrao;
import comum.model.persistent.Telegram;
import comum.model.persistent.User;
import comum.model.persistent.telegram.Location;
import comum.model.persistent.telegram.TelegramMessage;
import comum.model.persistent.telegram.TradeMessage;
import comum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.model.InMemoryMultipartFile;
import telegram.service.clients.UploadToContatos;
import telegram.utilitarios.Utilitarios;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    TelegramBotService botService;
    //    @Autowired TODO NAO ESTA SENDO FEITO USO DA API
//    ApiClient apiClient;
    @Autowired
    ButtonService buttonService;
    @Autowired
    UserService userService;
    @Autowired
    RasaService rasaService;

    @Autowired
    TelegramMessageRepository messageRepository;
    @Autowired
    TelegramRepository telegramRepository;
    @Autowired
    AlertaRepository alertaRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    MensagemPadraoRepository mensagemPadraoRepository;
    @Autowired
    UploadToContatos uploadToContatos;

    @Autowired
    private ApplicationContext applicationContext;


    @Transactional
    public void processMessage(Message message) throws IOException {

        SendMessage send = new SendMessage();
        send.setChatId(message.getChatId());
        String response = "";

        if (!message.getText().startsWith("report_alert")) { // BOTAO ESTA COMENTADO NO TelegramBotService
            rasaService.talkToRasa(message);
            return;
//        }else if(message.getText().startsWith("/auth")){
//            System.out.println("Chat id: "+ message.getChatId());
//            authenticationService.testValidation();
//        } else if (message.getText().startsWith("/login")) { TODO NAO ESTA SENDO FEITO USO DA API
//            response = logInCommand(message);
//            send.setText(response);
//            botService.postMessage(send);
//            return;
        } else if (message.getText().startsWith("/comandos")) {
            send.setText(getComandos());
        } else if (message.getText().startsWith("/start")) {
            send.setText(getBoasVindasUsuarioDefesaCivil());
        } else {
            System.out.println("Nenhum Processamento encontrado para este comando/mensagem. Por favor, procure o Administrador do Sistema.");
            response = "Nenhum Processamento de Messagem encontrado. Por favor, procure o Administrador do Sistema.";
        }

//        todo: autenticar o usuário
//        Optional<User> userOptional = userService.authenticateUser(message.getChatId());
//        if(!userOptional.isPresent()){
//            send.setText(getSignUp());
//            botService.postMessage(send);
//            return;
//        }
//        User user = userOptional.get();


        save(message); //registrar a comunicação com o usuário

        if (!response.isBlank()) send.setText(response);

        botService.postMessage(send);
    }

    public void processFile(Document document) throws TelegramApiException, IOException {

        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        String filePath = botService.execute(getFile).getFilePath();
        InputStream is = new URL("https://api.telegram.org/file/bot" + "1381146971:AAHeNYPbgQ-NGtpyrVkdDPHkf2IokUe3WNc" + "/" + filePath).openStream();

        System.out.println("filePath" + filePath + " FileName" + document.getFileName());
//        CustomMultipartFile file = new CustomMultipartFile(is.readAllBytes(), "fileName.xlsx");
        InMemoryMultipartFile file = new InMemoryMultipartFile("fileName.xlsx", is.readAllBytes());

        System.out.println("Chegou aqui.");

        System.out.println(uploadToContatos.handleFileUpload(file));

//        java.io.File file = new java.io.File("C:\\Users\\I7_TV_Touch\\Downloads\\plano_chamada_temp.xlsx");
//        FileUtils.copyInputStreamToFile(is, file);
//
//        ContatosClient github = Feign.builder()
//                .encoder(new FormEncoder(new JacksonEncoder()))
//                .target(ContatosClient.class, "http://localhost:8082/user/excel");
//
//        github.upload(file);

//        File file = new FileItemFactory(is);
//        MultipartFile multi = new CommonsMultipartFile(is);
//        contatosClient.upload(is)
        is.close();


    }


//    //CallBACK dos botões
//    public void processButtonResponse(CallbackQuery callBack) {
//
//        String text = callBack.getData();
//        EditMessageText send = new EditMessageText();
//        send.setChatId(callBack.getMessage().getChatId());
//        send.setMessageId(callBack.getMessage().getMessageId());
//        send.setText(text);
//
//
//        if (callBack.getData().startsWith("update_")) {
//            send.setReplyMarkup(buttonService.modifyButtonForAction1(callBack.getData()));
//        } else if (callBack.getData().startsWith("delete_")) {
//            text = text.replace("delete_", "");
//            send.setText("Alerta excluído com Sucesso!\n" + send.getText());
//        } else if (callBack.getData().startsWith("nop")) {
//            return;
//        }
//        botService.postMessage(send);
//    }

    //CallBACK dos botões Usuarios Comuns
    public void processButtonResponseCommonUser(CallbackQuery callBack) throws Exception {

        String text = callBack.getData();
        EditMessageText send = new EditMessageText();
        send.setChatId(callBack.getMessage().getChatId().toString());
        send.setMessageId(callBack.getMessage().getMessageId());
        send.setText(text);

        if (callBack.getData().startsWith("search_latest")) {

            List<Alerta> lista = alertaRepository.findTop5ByIdAlerta();

            if (!lista.isEmpty()) {
                StringBuilder messageAlertasConcat = new StringBuilder();

                for (Alerta alerta : lista) {
                    messageAlertasConcat.append(alerta.getTituloAlerta()).append(" - ");
                    messageAlertasConcat.append(Utilitarios.formatarData(alerta.getDataAlerta())).append(" - ");
                    messageAlertasConcat.append(alerta.getTextoAlerta());
                    messageAlertasConcat.append("\n\n");
                }

                send.setText(messageAlertasConcat.toString());
            }

        } else if (callBack.getData().startsWith("activate_alerts")) {

            Telegram userWhoSendMessage = criarTelegramUserCasoNaoExista(callBack, callBack.getFrom().getId().longValue());

            if (userWhoSendMessage.getPermiteAlertas()) {
                userWhoSendMessage.setPermiteAlertas(false);
                send.setText("Alertas Desativados!\n");
            } else {
                userWhoSendMessage.setPermiteAlertas(true);
                send.setText("Alertas Ativados!\n");
            }

            telegramRepository.save(userWhoSendMessage);

        } else if (callBack.getData().startsWith("report_alert")) {
            MensagemPadrao mensagemPadrao = mensagemPadraoRepository.findMensagemPadraoByTipo(EnumTipoMensagem.PADRAO_RETORNO_LIGAR_192.toString());

            if (mensagemPadrao != null) {
                send.setText(mensagemPadrao.getDescricao());
            } else {
                send.setText("Disque 193!");
            }
        }

        botService.postMessage(send);
    }


    private Telegram criarTelegramUserCasoNaoExista(CallbackQuery callBack, Long idChat) {

        Telegram userWhoSendMessage = telegramRepository.findByTelegramId(idChat);

        if (userWhoSendMessage == null) {

            User user = User.builder()
                    .nickname(callBack.getFrom().getUserName())
                    .name(callBack.getFrom().getFirstName())
                    .atualizacao(LocalDateTime.now())
                    .register(LocalDateTime.now())
                    .build();


            userWhoSendMessage = Telegram.builder()
                    .permiteAlertas(true)
                    .enabled(true)
                    .telegramId(callBack.getFrom().getId().longValue())
                    .created(LocalDateTime.now())
                    .build();

            userService.userRepository.save(user);

            userWhoSendMessage.setUser(user);

            telegramRepository.save(userWhoSendMessage);

            return userWhoSendMessage;

        }

        return userWhoSendMessage;

    }

    private Telegram criarTelegramUserCasoNaoExistaLocalizacao(Message message, Long idChat) {

        Telegram userWhoSendMessage = telegramRepository.findByTelegramId(idChat);

        if (userWhoSendMessage == null) {

            User user = User.builder()
                    .nickname(message.getFrom().getUserName())
                    .name(message.getFrom().getFirstName())
                    .atualizacao(LocalDateTime.now())
                    .register(LocalDateTime.now())
                    .build();


            userWhoSendMessage = Telegram.builder()
                    .permiteAlertas(true)
                    .enabled(true)
                    .telegramId(message.getFrom().getId().longValue())
                    .created(LocalDateTime.now())
                    .build();

            userService.userRepository.save(user);

            userWhoSendMessage.setUser(user);

            telegramRepository.save(userWhoSendMessage);

            return userWhoSendMessage;

        }

        return userWhoSendMessage;

    }

    public String getComandos() {
        return "Exemplos de uso dos Comandos disponíveis: " +
                "\n/alerta_novo {-1PETRA20} {+1PETRL20} {<=} {0.20}" +
                "\n/alerta_listar" +
                "\n/login usuario senha";
    }

    public String getBoasVindasUsuarioDefesaCivil() {
        return "Olá! Seja bem-vindo(a)! " +
                "\nEspero que esteja tudo bem por aí. Estou aqui para auxilia-lo." +
                "\nInicialmente é necessário vincular o seu Telegram ao seu Login do Sistema Integrado de Defesa Civil. " +
                "\nPara isto, digite o seguinte comando '\"/login\"' seguido do seu usuario e senha do Sistema de Defesa Civil" +
                "\nExemplo:" +
                "\n/login usuario senha";
    }

    public String getSignUp() {
        return "Olá! Infelizmente não consegui identificar seu usuário em minha base de dados. " +
                "\nInicialmente é necessário vincular o seu Telegram a Conta do site RendaContínua.com" +
                "\nPara isto, digite o seguinte comando: \n'\"/login\"' seguido do seu usuario e senha do site RendaContinua.Com" +
                "\nExemplo:" +
                "\n/login usuario senha";
    }

    @PreAuthorize("hasRole('user')")
    public void testAuthentication() {

        System.out.println("Entrou aqui! Ok.");
    }

//    public String logInCommand(Message message) { TODO COMENTADO POIS NAO ESTA SENDO FEITO USO DA API
//
//        String user = "";
//        String password = "";
//        String[] parts = message.getText().split(" ");
//        if (parts.length == 1) {
//            return "Por favor, digite seu usuário e senha na mesma linha do Comando. \nExemplo:\n /login usuario senha \n\n" +
//                    "Tenha certeza de estar cadastrado no site RendaContinua.Com";
//        } else {
//            if (!parts[1].isBlank()) user = parts[1];
//            if (!parts[2].isBlank()) password = parts[2];
//
//            if (!user.isBlank() && !password.isBlank()) {
//                LoginFormTelegram loginForm = new LoginFormTelegram(user, password, message.getChatId());
//                try {
//                    TradeMessage response = apiClient.authenticateUser(loginForm);
//                    return response.getText();
//                } catch (Exception e) {
//                    System.out.println("Erro desconhecido vindo da API!");
//                }
//            }
//        }
//
//        return "Algum erro desconhecido aconteceu. Vc está cadastrado no site RendaContinua.Com?" +
//                " \nPor favor, digite seu usuário e senha na mesma linha do Comando." +
//                " \nExemplo:\n /login usuario senha " +
//                " \n\n\n Se o problema persistir, procure o Administrador do Sistema.";
//    }

    @Transactional
    public void save(Message message) {

        messageRepository.save(new TelegramMessage(message));
    }

    public void processLocation(Message message) {

        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getFrom().getId()));


            Telegram telegram = this.criarTelegramUserCasoNaoExistaLocalizacao(message, message.getFrom().getId().longValue());

            Location location = Location.builder()
                    .longitude(message.getLocation().getLongitude())
                    .latitude(message.getLocation().getLatitude())
                    .telegram(telegram)
                    .build();

            locationRepository.save(location);
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
            sendMessage.setText("Localização salva com sucesso!");
            botService.postMessage(sendMessage);

            System.out.println("Localização salva com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar localização!");
        }


    }
}
