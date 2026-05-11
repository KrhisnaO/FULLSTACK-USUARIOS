package com.fullstack3.fullstack3;

import com.fullstack3.fullstack3.exception.ResourceNotFoundException;
import com.fullstack3.fullstack3.model.Usuario;
import com.fullstack3.fullstack3.repository.UsuarioRepository;
import com.fullstack3.fullstack3.service.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UsuarioServiceImpl.
 * Cubre los métodos: listar, buscarPorId, guardar, actualizar, eliminar y login.
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioBase;

    @BeforeEach
    void setUp() {
        usuarioBase = new Usuario(
                1L, "Krhisna", "González", "López",
                "krhisna@mail.com", "Pass123!", "CLIENTE"
        );
    }

    // ---- listar() ----

    @Test
    void listar_retornaListaDeUsuarios() {
        Usuario u2 = new Usuario(2L, "Admin", "Pérez", "Soto",
                "admin@mail.com", "Admin123!", "ADMIN");
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioBase, u2));

        List<Usuario> resultado = usuarioService.listar();

        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void listar_sinUsuarios_retornaListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<Usuario> resultado = usuarioService.listar();

        assertTrue(resultado.isEmpty());
    }

    // ---- buscarPorId() ----

    @Test
    void buscarPorId_existente_retornaUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase));

        Optional<Usuario> resultado = usuarioService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Krhisna", resultado.get().getNombre());
    }

    @Test
    void buscarPorId_noExistente_retornaEmpty() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    // ---- guardar() ----

    @Test
    void guardar_usuarioValido_retornaUsuarioGuardado() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioBase);

        Usuario resultado = usuarioService.guardar(usuarioBase);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("krhisna@mail.com", resultado.getCorreo());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // ---- actualizar() ----

    @Test
    void actualizar_usuarioExistente_retornaUsuarioActualizado() {
        Usuario datosNuevos = new Usuario(
                null, "KrhisnaEdit", "González", "López",
                "nuevo@mail.com", "Nuevo123!", "ADMIN"
        );
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = usuarioService.actualizar(1L, datosNuevos);

        assertEquals("KrhisnaEdit", resultado.getNombre());
        assertEquals("nuevo@mail.com", resultado.getCorreo());
        assertEquals("ADMIN", resultado.getRol());
    }

    @Test
    void actualizar_usuarioNoExistente_lanzaRuntimeException() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> usuarioService.actualizar(99L, usuarioBase));

        verify(usuarioRepository, never()).save(any());
    }

    // ---- eliminar() ----

    @Test
    void eliminar_usuarioExistente_lllamaDelete() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase));
        doNothing().when(usuarioRepository).delete(usuarioBase);

        assertDoesNotThrow(() -> usuarioService.eliminar(1L));

        verify(usuarioRepository, times(1)).delete(usuarioBase);
    }

    @Test
    void eliminar_usuarioNoExistente_lanzaRuntimeException() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> usuarioService.eliminar(99L));

        verify(usuarioRepository, never()).delete(any());
    }

    // ---- login() ----

    @Test
    void login_credencialesCorrectas_retornaUsuario() {
        when(usuarioRepository.findByCorreoAndPassword("krhisna@mail.com", "Pass123!"))
                .thenReturn(Optional.of(usuarioBase));

        Optional<Usuario> resultado = usuarioService.login("krhisna@mail.com", "Pass123!");

        assertTrue(resultado.isPresent());
        assertEquals("CLIENTE", resultado.get().getRol());
    }

    @Test
    void login_credencialesIncorrectas_retornaEmpty() {
        when(usuarioRepository.findByCorreoAndPassword("krhisna@mail.com", "wrongpass"))
                .thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.login("krhisna@mail.com", "wrongpass");

        assertFalse(resultado.isPresent());
    }

    @Test
    void login_correoInexistente_retornaEmpty() {
        when(usuarioRepository.findByCorreoAndPassword("noexiste@mail.com", "Pass123!"))
                .thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.login("noexiste@mail.com", "Pass123!");

        assertFalse(resultado.isPresent());
    }
}