package com.fullstack3.fullstack3.repository;

import com.fullstack3.fullstack3.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // BUSCA POR CORREO
    Optional<Usuario> findByCorreo(String correo);

    // LOGIN: BUSCA POR CORREO Y CONTRASEÑA
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);

}