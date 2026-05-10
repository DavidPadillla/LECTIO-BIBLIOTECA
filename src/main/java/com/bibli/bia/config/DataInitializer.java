package com.bibli.bia.config;

import com.bibli.bia.Model.Usuario;
import com.bibli.bia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Verificar si ya existe al menos un ADMIN
        boolean existeAdmin = usuarioRepository.findAll().stream()
                .anyMatch(usuario -> usuario.getRoles() != null && usuario.getRoles().contains("ADMIN"));

        if (!existeAdmin) {
            // Crear usuario ADMIN por defecto
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of("ADMIN"));
            usuarioRepository.save(admin);
            System.out.println("✅ Usuario ADMIN creado: admin / admin123");
        } else {
            System.out.println("⚠️ Ya existe un usuario ADMIN, no se crea duplicado.");
        }

        // Opcional: Crear un usuario USER de prueba si no existe
        boolean existeUser = usuarioRepository.findAll().stream()
                .anyMatch(usuario -> usuario.getRoles() != null && usuario.getRoles().contains("USER"));

        if (!existeUser) {
            Usuario user = new Usuario();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRoles(Set.of("USER"));
            usuarioRepository.save(user);
            System.out.println("✅ Usuario USER creado: user / user123");
        }
    }
}