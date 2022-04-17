package telegram.service;

import comum.model.dto.rasa.RasaMessage;
import comum.model.dto.rasa.RasaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import telegram.service.clients.RasaClient;

import java.util.List;

@Service
public class RasaService {

    @Autowired
    RasaClient rasaClient;
    @Autowired
    TelegramBotService telegramBotService;

    public void talkToRasa(Message message){

        try{
            List<RasaResponse> responses =  rasaClient.sendMessage(new RasaMessage(message));
            for(RasaResponse response: responses){
                SendMessage send = new SendMessage();
                send.setChatId(response.getRecipient_id());
                send.setText(response.getText());
                telegramBotService.postMessage(send);
            }

        }catch (Exception e){
            System.out.println("Erro ao comunicar com Rasa: "+e);

            SendMessage send = new SendMessage();
            send.setChatId(message.getChatId());
            send.setText("Erro ao comunicar do Telegram para o ChatBot. Tente novamente ou comunique o Administrador " +
                    "do Sistema.");
            telegramBotService.postMessage(send);
        }
    }

    public void sendMessage(Long chatId){



    }



}
