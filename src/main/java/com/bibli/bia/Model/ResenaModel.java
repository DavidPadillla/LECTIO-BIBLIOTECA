package com.bibli.bia.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "resenas")
public class ResenaModel {

    @Id
    private String id;

    private String nombre;
    private String comentario;


    public ResenaModel(String nombre, String comentario) {
        this.nombre = nombre;
        this.comentario = comentario;
    }
}
