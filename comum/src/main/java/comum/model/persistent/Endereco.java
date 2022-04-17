package comum.model.persistent;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "public", name = "endereco")
public class Endereco {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    @NotEmpty
    private String cep;

    //@NotNull
    @Column(name = "endereco_tipo", length = 11)
    //@Enumerated(EnumType.STRING)
    //@NotNull
    private String tipoDeEndereco;

    @Column(name = "prioridade")
    //@NotNull
    private String prioridade;

    @Column(name = "logradouro", length = 40)
//    @NotNull
//    @NotEmpty
    private String logradouro;

    @Column(name = "numero", length = 5)
    private String numero;

    @Column(name = "lote", length = 5)
    private String lote;

    @Column(name = "quadra", length = 7)
    private String quadra;

    private String complemento;

    private String referencia;

    @NotNull
    @NotEmpty
    private String bairro;

    @NotNull
    @NotEmpty
    private String municipio;

    @NotNull
    @NotEmpty
    private String estado;

    @NotNull
    @NotEmpty
    private String pais;

    @AllArgsConstructor
    public enum EnderecoTipo {

        RESIDENCIAL( "Residencial"),
        COMERCIAL( "Comercial"),
        OUTRO( "Outro");


        @Getter
        private String label;


        public static EnderecoTipo toEnum(String label) {
            switch (label) {
                case "Residencial":
                    return EnderecoTipo.RESIDENCIAL;
                case "Comercial":
                    return EnderecoTipo.COMERCIAL;
                case "Outro":
                    return EnderecoTipo.OUTRO;
                default:
                    throw new IllegalArgumentException("toEnum: Tipo De Endereco inválido");
            }
        }

    }


}
