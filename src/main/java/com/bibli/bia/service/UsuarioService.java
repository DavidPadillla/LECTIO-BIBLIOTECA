package com.bibli.bia.service;

import com.bibli.bia.Model.Usuario;
import com.bibli.bia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SyncService realTimeSyncService;  // ← Solo agregar esto

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {
        Usuario saved = usuarioRepository.save(usuario);
        // Sincronizar automáticamente a Neon
        realTimeSyncService.sincronizarUsuario(saved);
        return saved;
    }
}