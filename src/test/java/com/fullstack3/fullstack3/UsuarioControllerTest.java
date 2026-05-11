package com.fullstack3.fullstack3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack3.fullstack3.controller.UsuarioController;
import com.fullstack3.fullstack3.model.Usuario;
import com.fullstack3.fullstack3.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para UsuarioController usando MockMvc.
 * Verifica los endpoints REST: listar, buscarPorId, crear, login, actualizar y eliminar.
 */
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario buildUsuario() {
        return new Usuario(
                1L, "Krhisna", "González", "López",
                "krhisna@mail.com", "Pass123!", "CLIENTE"
        );
    }

    // ---- GET /api/usuarios ----

    @Test
    void listar_retorna200ConListaDeUsuarios() throws Exception {
        Usuario u2 = new Usuario(2L, "Admin", "Pérez", "Soto",
                "admin@mail.com", "Admin123!", "ADMIN");
        when(usuarioService.listar()).thenReturn(List.of(buildUsuario(), u2));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ---- GET /api/usuarios/{id} ----

    @Test
    void obtenerPorId_existente_retorna200() throws Exception {
        when(usuarioService.buscarPorId(1L)).thenReturn(Optional.of(buildUsuario()));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.correo").value("krhisna@mail.com"));
    }

    @Test
    void obtenerPorId_noExistente_retorna404() throws Exception {
        when(usuarioService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    // ---- POST /api/usuarios ----

    @Test
    void crear_usuarioValido_retorna201() throws Exception {
        when(usuarioService.guardar(any(Usuario.class))).thenReturn(buildUsuario());

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUsuario())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    // ---- POST /api/usuarios/login ----

    @Test
    void login_credencialesCorrectas_retorna200() throws Exception {
        when(usuarioService.login("krhisna@mail.com", "Pass123!"))
                .thenReturn(Optional.of(buildUsuario()));

        Usuario loginReq = new Usuario();
        loginReq.setCorreo("krhisna@mail.com");
        loginReq.setPassword("Pass123!");

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("krhisna@mail.com"));
    }

    @Test
    void login_credencialesIncorrectas_retorna401() throws Exception {
        when(usuarioService.login("krhisna@mail.com", "wrongpass"))
                .thenReturn(Optional.empty());

        Usuario loginReq = new Usuario();
        loginReq.setCorreo("krhisna@mail.com");
        loginReq.setPassword("wrongpass");

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isUnauthorized());
    }

    // ---- PUT /api/usuarios/{id} ----

    @Test
    void actualizar_usuarioExistente_retorna200() throws Exception {
        when(usuarioService.actualizar(eq(1L), any(Usuario.class)))
                .thenReturn(buildUsuario());

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUsuario())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_usuarioNoExistente_retorna404() throws Exception {
        when(usuarioService.actualizar(eq(99L), any(Usuario.class)))
                .thenThrow(new RuntimeException("Usuario no encontrado con id: 99"));

        mockMvc.perform(put("/api/usuarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildUsuario())))
                .andExpect(status().isNotFound());
    }

    // ---- DELETE /api/usuarios/{id} ----

    @Test
    void eliminar_usuarioExistente_retorna204() throws Exception {
        doNothing().when(usuarioService).eliminar(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_usuarioNoExistente_retorna404() throws Exception {
        doThrow(new RuntimeException("Usuario no encontrado con id: 99"))
                .when(usuarioService).eliminar(99L);

        mockMvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }
}