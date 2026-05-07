package com.bibli.bia.postgres.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class UsuarioPostgres {

    @Id
    @Column(columnDefinition = "TEXT")
    private String id;

    @Column(name = "username", columnDefinition = "TEXT", unique = true, nullable = false)
    private String username;

    @Column(name = "password", columnDefinition = "TEXT", nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String roles;  // JSON: ["ROLE_USER","ROLE_ADMIN"]

    // ✅ OPCIONAL: Agregar campos útiles para Power BI
    @Column(name = "email", columnDefinition = "TEXT")
    private String email;

    @Column(name = "fecha_registro")
    private java.time.LocalDateTime fechaRegistro;

    @Column(name = "activo")
    private Boolean activo = true;

    // Constructor original (sin los nuevos campos)
    public UsuarioPostgres(String username, String password, String roles) {
        this.id = java.util.UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email = null;
        this.fechaRegistro = java.time.LocalDateTime.now();
        this.activo = true;
    }

    // ✅ Constructor completo (recomendado)
    public UsuarioPostgres(String username, String password, String roles, String email) {
        this.id = java.util.UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email = email;
        this.fechaRegistro = java.time.LocalDateTime.now();
        this.activo = true;
    }
}