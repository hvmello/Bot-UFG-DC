package comum.model.persistent.rural;


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
@Table(schema = "rural", name = "colaborador")
public class Colaborador {

    @Id
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriedade_id")
    Propriedade propriedade;
    String nome;
    String cpf;
    String rg;
    String sexo;
    String tipopess;
    String naturalidade;
    String pai;
    String mae;
    String dn;
    String foto;
    String obs;
    String endereco;


}
