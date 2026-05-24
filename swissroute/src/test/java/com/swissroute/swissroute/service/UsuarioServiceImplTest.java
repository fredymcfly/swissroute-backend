package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.RegistroRequest;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.exception.UsuarioYaExisteException;
import com.swissroute.swissroute.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private RegistroRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new RegistroRequest();
        validRequest.setNombre("Juan Pérez");
        validRequest.setEmail("juan.perez@example.com");
        validRequest.setPassword("password123");
        validRequest.setCiudadBase("Zurich");
    }

    @Test
    void registro_Success_ReturnsUsuarioResponse() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            usuario.setCreatedAt(LocalDateTime.now());
            return usuario;
        });

        // When
        var result = usuarioService.registro(validRequest);

        // Then
        assertNotNull(result);
        assertEquals("Juan Pérez", result.getNombre());
        assertEquals("juan.perez@example.com", result.getEmail());
        assertEquals("Zurich", result.getCiudadBase());
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
        verify(usuarioRepository, times(1)).existsByEmail("juan.perez@example.com");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registro_EmailAlreadyExists_ThrowsUsuarioYaExisteException() {
        // Given
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(UsuarioYaExisteException.class, () -> {
            usuarioService.registro(validRequest);
        });

        verify(usuarioRepository, times(1)).existsByEmail("juan.perez@example.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}