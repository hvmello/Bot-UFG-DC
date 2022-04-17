package comum.model.persistent.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import comum.model.persistent.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data //set e get
@NoArgsConstructor //metodo construtor sem nenhum atributo
@AllArgsConstructor //metodo construtor com todos os atributos
@Entity //indica que esta classe é mapeada para o Banco de Dados
@Table(schema = "project", name = "atividade") //indicar o schema e nome da tabela
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private User user;

    @Column(name="atividade")
    private String nome;


    @Column(name="tipo")
    @Enumerated(EnumType.STRING)
    private TipoDeAtividade tipoDeAtividade;


    @Column(name="inicio", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime inicio;


    @Column(name="prazo", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime prazo;

    @Column(name="criado_em", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime criadoEm = LocalDateTime.now();


    public enum TipoDeAtividade{
        Evento,
        Tarefa,
        Lembrete
    }

}
