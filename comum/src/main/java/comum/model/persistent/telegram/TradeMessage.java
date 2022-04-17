package comum.model.persistent.telegram;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TradeMessage {

    private Long chatId;

    private String text;

    private Boolean sucess;

    private StringBuilder stringBuilder;
}
