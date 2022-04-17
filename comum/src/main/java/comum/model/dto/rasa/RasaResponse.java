package comum.model.dto.rasa;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RasaResponse {

    private String recipient_id;
    private String text;

}
