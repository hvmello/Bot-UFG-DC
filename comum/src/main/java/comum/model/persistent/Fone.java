package comum.model.persistent;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "public", name = "fone")
public class Fone {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    //@NotEmpty
    private String ddi;

    //@NotNull
    //@NotEmpty
    private String ddd;

    //@NotNull
    //@NotEmpty
    private String number;

    //@NotNull
    @Column(name = "telefone_tipo", length = 11)
    @Enumerated(EnumType.STRING)
    private FoneTipo phoneType;

    @Column(name = "principal")
    private Boolean principal;


    @AllArgsConstructor
    public enum FoneTipo {

        CELULAR("Celular"),
        RESIDENCIAL( "Residencial"),
        COMERCIAL( "Comercial"),
        OUTRO( "Outro");



        @Getter
        private String label;

        public static FoneTipo toEnum(String label) {
            switch (label) {
                case "Residencial":
                    return FoneTipo.RESIDENCIAL;
                case "Comercial":
                    return FoneTipo.COMERCIAL;
                case "Celular":
                    return FoneTipo.CELULAR;
                case "Outro":
                    return FoneTipo.OUTRO;
                default:
                    throw new IllegalArgumentException("toEnum: FoneTipo inválido");
            }
        }

    }

}
