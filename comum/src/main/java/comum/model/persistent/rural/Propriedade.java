package comum.model.persistent.rural;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "rural", name = "propriedade")
public class Propriedade {

    @Id
    Long id;
    Long id_atividade;
    String cidade;
    String nome;
    Double latitude;
    Double longitude;
    Long nr_propriedade;
    String referencia;
    String zona_rural;
    String redewifi;
    String senhawifi;
    String cod_propriedade;
    String defensivo;
    String def_mes_ini;
    String def_mes_fim;
    String imei;
    String mobid;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime dataDeRegistro;
    String nr_rai_equipe;
    String lancado;


    public Propriedade (Long id){
        this.id = id;
    }

}
