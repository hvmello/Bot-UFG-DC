package comum.model.persistent;

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
@Table(schema = "geomaps", name = "cidade")
public class Municipio {

    @Id //código do IBGE
    private Long id;

    private String municipio;

    private String uf;

    private Integer ufCodigo;

}
