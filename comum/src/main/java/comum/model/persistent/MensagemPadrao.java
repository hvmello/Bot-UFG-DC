package comum.model.persistent;

import comum.model.enums.EnumTipoMensagem;
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
@Table(schema = "public", name = "mensagempadrao")
public class MensagemPadrao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "tipoalerta")
    @Enumerated(EnumType.STRING)
    private EnumTipoMensagem tipo;


}
