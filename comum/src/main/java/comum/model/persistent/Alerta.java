package comum.model.persistent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import comum.model.enums.EnumTipoAlerta;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "public", name = "alerta")
public class Alerta {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlerta;

    @Column(name = "tituloalerta")
    private String tituloAlerta;

    @Column(name = "tipoalerta")
    @Enumerated(EnumType.STRING)
    private EnumTipoAlerta tipo;

    @Column(name = "textoalerta")
    private String textoAlerta;

    @Column(name="dataalerta", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dataAlerta;


    @Transient
    private Float latitudeTop;

    @Transient
    private Float latitudeBottom;

    @Transient
    private Float longitudeLeft;

    @Transient
    private Float longitudeRight;



}
