package com.fullstack3.fullstack3.service;

import com.fullstack3.fullstack3.model.Usuario;
import com.fullstack3.fullstack3.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listar() {
        logger.info("Service: OBTENIENDO LISTA DE USUARIOS");
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        logger.info("Service: BUSCANDO USUARIO POR ID: {}", id);
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        logger.info("Service: GUARDAR NUEVO USUARIO, CORREO: {}", usuario.getCorreo());
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        logger.info("Service: ACTUALIZANDO USUARIO ID: {}", id);

        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        existente.setNombre(usuario.getNombre());
        existente.setApaterno(usuario.getApaterno());
        existente.setAmaterno(usuario.getAmaterno());
        existente.setCorreo(usuario.getCorreo());
        existente.setPassword(usuario.getPassword());
        existente.setRol(usuario.getRol());

        return usuarioRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        logger.warn("Service: ELIMINANDO USUARIO ID: {}", id);

        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        usuarioRepository.delete(existente);
    }

    @Override
    public Optional<Usuario> login(String correo, String password) {
        logger.info("Service: INTENTO DE LOGIN PARA EL CORREO: {}", correo);
        return usuarioRepository.findByCorreoAndPassword(correo, password);
    }
}