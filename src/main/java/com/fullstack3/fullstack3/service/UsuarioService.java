package com.fullstack3.fullstack3.service;

import com.fullstack3.fullstack3.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listar();

    Optional<Usuario> buscarPorId(Long id);

    Usuario guardar(Usuario usuario);

    Usuario actualizar(Long id, Usuario usuario);

    void eliminar(Long id);

    Optional<Usuario> login(String correo, String password);
}