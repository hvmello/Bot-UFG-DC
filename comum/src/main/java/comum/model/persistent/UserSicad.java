package comum.model.persistent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "public", name = "user_sicad") //o postgres não aceita criar tabela com o nome "user"
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class) //habilitando jsonb para JPA
public class UserSicad {

    @Id
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "username")
    private String username;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<String> funcoes = new ArrayList<>();

    private String atualizacao;

    private String obm;

    private String regional;

    private String patente;

    @Column(name = "quadro")
    private String quadroFuncional;

    @NotNull
    @Column(unique = true)
    private Integer rg;

    private String nome;

    @Column(name = "nome_guerra")
    private String nomeDeGuerra;

    private String celular;

    private String telefeneResidencial;


    @Column(unique = true)
    private String email;


    @Column(unique = true)
    private String email1;

    private String nascimento;

    @NotNull
    @Column(unique = true)
    private String cpf;

    private String sexo;

    private String status;

    private Boolean operacional; //operacional = true, adm = false

    @Column(name = "ausencia_motivo")
    private String ausenciaMotivo;

    @JsonFormat(pattern = "dd/MM/yy")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "ausencia_inicio", columnDefinition = "TIMESTAMP")//, nullable = false)
    private LocalDate ausenciaInicio;

    @JsonFormat(pattern = "dd/MM/yy")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "ausencia_fim", columnDefinition = "TIMESTAMP")//, nullable = false)
    private LocalDate ausenciaFim;

    @JsonFormat(pattern = "dd/MM/yy")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "inclusao", columnDefinition = "TIMESTAMP")//, nullable = false)
    private LocalDate inclusao;

    private String municipio;

    private String uf;



}
