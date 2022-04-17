package comum.model.persistent.telegram;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "chatbot", name = "telegram_command")
public class TelegramCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique=true, nullable=false)
    private String command;

    @Column(name= "isform", unique=true, nullable=false)
    private Boolean isForm;

    private StringBuilder form;
}
