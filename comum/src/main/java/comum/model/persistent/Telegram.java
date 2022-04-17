package comum.model.persistent;

import comum.model.dto.security.LoginFormTelegram;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "public", name = "telegram")
public class Telegram {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_pk", referencedColumnName = "id" /*(id do User)*/, nullable = false)
    private User user;

    @Column(name = "principal")
    private Boolean principal;

    @Column(name = "telegram_habilitado")
    private Boolean enabled;

    @Column(name = "permite_alertas")
    private Boolean permiteAlertas;

    @Column(name = "telegram_id", unique = true)
    private Long telegramId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "created", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    public Telegram(LoginFormTelegram loginRequest, User user){
        this.enabled = true;
        this.telegramId = loginRequest.getTelegramId();
        this.user = user;
    }

    public Telegram(Long telegramId){
        this.telegramId = telegramId;
    }


}
