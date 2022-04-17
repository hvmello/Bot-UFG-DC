package comum.model.persistent.telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(schema = "chatbot", name = "telegram_edit_message")
public class TelegramEditMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "message_id")
    private Integer messageId;

    @Column(name = "text")
    private String text;

    @Column(name="json_object")
    private String jsonObject;
}
