/*package com.bibli.bia.config;

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
        // Contar cuántos admins existen
        long cantidadAdmins = usuarioRepository.count();

        // Generar un username único
        String nuevoUsername = "david" + (cantidadAdmins + 1);

        // Verificar que no exista ya un usuario con ese username
        if (usuarioRepository.findByUsername(nuevoUsername).isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername(nuevoUsername);
            admin.setPassword(passwordEncoder.encode("1234567")); // Contraseña por defecto
            admin.setRoles(Set.of("ADMIN"));
            usuarioRepository.save(admin);

            System.out.println("✅ Se creó automáticamente el usuario: " + nuevoUsername);
        } else {
            System.out.println("⚠️ El usuario " + nuevoUsername + " ya existe, no se crea duplicado.");
        }
    }
}*/
