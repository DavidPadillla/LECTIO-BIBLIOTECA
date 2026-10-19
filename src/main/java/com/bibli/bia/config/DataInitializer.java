package com.bibli.bia.config;

import com.bibli.bia.Model.Usuario;
import com.bibli.bia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        Optional<Usuario> adminExistente =
                usuarioRepository.findByUsername("admin");

        if (adminExistente.isEmpty()) {

            Usuario admin = new Usuario();

            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of("ADMIN"));
            admin.setActivo(true);

            usuarioRepository.save(admin);

            System.out.println("=================================");
            System.out.println("✅ ADMIN CREADO");
            System.out.println("Usuario: admin");
            System.out.println("Contraseña: admin123");
            System.out.println("Rol: ADMIN");
            System.out.println("Estado: ACTIVO");
            System.out.println("=================================");

        } else {

            System.out.println("⚠️ El usuario admin ya existe.");
        }
    }
}