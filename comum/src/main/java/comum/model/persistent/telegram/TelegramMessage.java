package comum.model.persistent.telegram;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import comum.model.persistent.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "chatbot", name = "telegram_message")
public class TelegramMessage {

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

    @Column(name = "type")
    @Enumerated (EnumType.STRING)
    private Type type;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "inserted", columnDefinition = "TIMESTAMP")//, nullable = false)
    private LocalDateTime inserted = LocalDateTime.now();


    public TelegramMessage(Message message){

        this.chatId     = message.getChatId();
        this.text       = message.getText();
        this.messageId  = message.getMessageId();
    }

    @AllArgsConstructor
    private enum Type {

        ASK( "Pergunta do Usuário"),
        ANSWER( "Resposta do Bot");


        @Getter
        private String label;

        public static Type toEnum(String label) {
            switch (label) {
                case "Pergunta do Usuário":
                    return Type.ASK;
                case "Resposta do Bot":
                    return Type.ANSWER;
                default:
                    throw new IllegalArgumentException("Telegram Message: tipo Inválido");
            }
        }
    }




}
