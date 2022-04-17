package comum.model.persistent.telegram;

import comum.model.persistent.Telegram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "chatbot", name = "telegram_location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = Telegram.class)
    @JoinColumn(name = "telegram_pk", referencedColumnName = "id" /*(id do User)*/, nullable = false)
    private Telegram telegram;

    @Column(name="longitude", nullable=false)
    private Float longitude;

    @Column(name="latitude", nullable=false)
    private Float latitude;

    @Column(name="horizontal_accuracy")
    private Integer horizontalAccuracy;

    @Column(name="live_period")
    private Integer livePeriod;

    @Column(name="heading")
    private Integer heading;

    @Column(name="proximity_alert_radius")
    private Integer proximityAlertRadius;


}
