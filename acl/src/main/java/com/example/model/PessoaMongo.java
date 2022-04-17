package com.example.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data //set e get
@NoArgsConstructor //metodo construtor sem nenhum atributo
@AllArgsConstructor //metodo construtor com todos os atributos
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "pessoas")
public class PessoaMongo {

    @Id
    private String id;

    @Field(name = "name")
    private String name;
}
