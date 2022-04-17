package telegram.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ButtonService {

    private static InlineKeyboardButton[][] tabela;


    public InlineKeyboardMarkup getButtonForAction1() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(getButton());
        return markupInline;
    }

    public SendMessage modifyButtonForAction(CallbackQuery callBack) {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(callBack.getFrom().getId().longValue());

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardButton keyboardButton = new KeyboardButton();
        List<KeyboardRow> row = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardButton.setRequestLocation(true);
        keyboardButton.setText("Enviar Localização");
        keyboardRow.add(keyboardButton);
        row.add(keyboardRow);
        markup.setKeyboard(row);

        message.setReplyMarkup(markup);
        message.setText("Enviar Localização");

        return message;
    }


    //Este botão inicial vale para qualquer usuário
//    public List<List<InlineKeyboardButton>> getButton(){
//        tabela     = new InlineKeyboardButton[2][2];
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        for(int line = 0; line <=1; line++){
//            List<InlineKeyboardButton> rowInline = new ArrayList<>();
//            for(int column=0; column<=1; column++){
//                tabela[line][column] = new InlineKeyboardButton().setText("Btn_"+line+"_"+column).setCallbackData("update_"+line+"_"+column);
//                rowInline.add(tabela[line][column] );
//            }
//
//            rowsInline.add(rowInline);
//        }
//        return rowsInline;
//    }

    public List<List<InlineKeyboardButton>> getButton() {


        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        tabela = new InlineKeyboardButton[1][4];
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Consultar Últimos Alertas").setCallbackData("search_for_last_updates"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);


        return rowsInline;
    }


    public List<List<InlineKeyboardButton>> convertArrayToListButtons() {

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (int line = 0; line <= 1; line++) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            for (int column = 0; column <= 1; column++) {
                rowInline.add(tabela[line][column]);
            }
            rowsInline.add(rowInline);
        }
        return rowsInline;
    }

//    public void modifyButtons(String callBack) {
//
//        SendMessage message = new SendMessage() // Create a message object object
//                .setChatId();
//
//
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//
//
//
//    }

    public String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
    }

}
