package com.bibli.bia.service;

import com.bibli.bia.repository.UsuarioRepository;
import com.bibli.bia.Model.Usuario;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // ✅ USAR EL MÉTODO OPTIMIZADO que carga roles con JOIN FETCH
        Usuario usuario = usuarioRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        log.info("✅ Usuario cargado correctamente: {}", usuario.getUsername());
        log.debug("Roles del usuario {}: {}", usuario.getUsername(), usuario.getRoles());

        // Convertir roles a autoridades de Spring Security
        List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                authorities
        );
    }
}