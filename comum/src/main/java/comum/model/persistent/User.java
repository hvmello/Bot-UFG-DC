package comum.model.persistent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import comum.model.dto.security.SignUpForm;
import lombok.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Table(schema = "public", name = "users") //o postgres não aceita criar tabela com o nome "user"
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "keycloak_id")
    private String keycloakId;

    @Email
    //@NotNull
    @Column(name = "email", length = 50)
    private String email;

    //@NotNull
    //@NotEmpty
    @Column(name = "username", length = 25)
    private String username;

//    @NotNull
//    @NotEmpty
    @Column(name = "name", length = 80)
    private String name;

    @Column(name = "nick_name", length = 80)
    private String nickname;

    @CPF
//    @NotNull
//    @NotEmpty
    @Column(name = "cpf", length = 14)
    private String cpf;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past
//    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "nascimento")
    private LocalDate nascimento;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private List<Endereco> enderecos;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private List<Fone> fones;

    @OneToOne
    @Transient
    private Telegram telegram;

    //@NotNull
    @Column(name = "sexo")
    @Enumerated (EnumType.STRING)
    private Sexo sexo;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "register")
    private LocalDateTime register;


    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "atualizacao")
    private LocalDateTime atualizacao;

    @AllArgsConstructor
    private enum Sexo {

        MASCULINO("M", "Masculino"),
        FEMININO("F", "Feminino");

        @Getter
        private String sigla;

        @Getter
        private String label;

        public static Sexo toEnum(String sigla) {
            switch (sigla) {
                case "M":
                    return Sexo.MASCULINO;
                case "F":
                    return Sexo.FEMININO;
                default:
                    throw new IllegalArgumentException("Valor da sigla de Enumerador Sexo inválido");
            }
        }

    }

    public User(Long id){
        this.id = id;
    }

    public User(SignUpForm u, String keycloakId){
        this.keycloakId         = keycloakId;
        this.name               = u.getName();
        this.username           = u.getUsername();
        this.email              = u.getEmail();
        this.register           = LocalDateTime.now();
        this.atualizacao        = LocalDateTime.now();
    }


    public User( UserSicad u){
        this.username = u.getRg().toString();
        this.email = u.getEmail();
        this.name = u.getNome();

    }


    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }


}





