package comum.model.persistent.rural;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "rural", name = "contatos")
public class Contatos {

    @Id
    Long id;
    Long id_pessoa;
    String telefone;
    String email;

}
