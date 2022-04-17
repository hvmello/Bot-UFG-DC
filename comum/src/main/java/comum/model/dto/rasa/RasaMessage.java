package comum.model.dto.rasa;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RasaMessage {

    private String sender;
    private String message;

    public RasaMessage(Message message){
        this.sender  = message.getChatId().toString();
        this.message = message.getText();
    }
}
