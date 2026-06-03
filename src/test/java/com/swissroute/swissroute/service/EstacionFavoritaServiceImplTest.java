package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.EstacionFavoritaRequest;
import com.swissroute.swissroute.dto.EstacionFavoritaResponse;
import com.swissroute.swissroute.entity.EstacionFavorita;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.exception.EstacionNoEncontradaException;
import com.swissroute.swissroute.exception.EstacionYaExisteException;
import com.swissroute.swissroute.repository.EstacionFavoritaRepository;
import com.swissroute.swissroute.repository.UsuarioRepository;
import com.swissroute.swissroute.service.impl.EstacionFavoritaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EstacionFavoritaServiceImplTest {

    @Mock
    private EstacionFavoritaRepository estacionFavoritaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EstacionFavoritaServiceImpl estacionFavoritaService;

    private Usuario mockUsuario;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        mockUsuario = new Usuario(1L, "John Doe", "john@example.com", "password123", "Zurich", LocalDateTime.now());
        
        authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("crearEstacionFavorita - Debe crear una estación favorita exitosamente")
    void crearEstacionFavorita_Success() {
        // Given
        EstacionFavoritaRequest request = new EstacionFavoritaRequest();
        request.setEstacionIdExterno("station123");
        request.setNombre("Zurich Hauptbahnhof");

        when(usuarioRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUsuario));
        when(estacionFavoritaRepository.existsByUsuarioIdAndEstacionIdExterno(anyLong(), anyString())).thenReturn(false);
        
        EstacionFavorita savedEstacion = new EstacionFavorita();
        savedEstacion.setId(1L);
        savedEstacion.setEstacionIdExterno("station123");
        savedEstacion.setNombreEstacion("Zurich Hauptbahnhof");
        savedEstacion.setUsuario(mockUsuario);
        savedEstacion.setUsuarioId(1L);
        savedEstacion.setCreatedAt(LocalDateTime.now());
        
        when(estacionFavoritaRepository.save(any(EstacionFavorita.class))).thenReturn(savedEstacion);

        // When
        EstacionFavoritaResponse result = estacionFavoritaService.crearEstacionFavorita(request);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("station123", result.getEstacionIdExterno());
        assertEquals("Zurich Hauptbahnhof", result.getNombre());
        verify(usuarioRepository).findByEmail("john@example.com");
        verify(estacionFavoritaRepository).existsByUsuarioIdAndEstacionIdExterno(1L, "station123");
        verify(estacionFavoritaRepository).save(any(EstacionFavorita.class));
    }

    @Test
    @DisplayName("crearEstacionFavorita - Debe lanzar EstacionYaExisteException cuando la estación ya existe")
    void crearEstacionFavorita_Duplicate_ThrowsException() {
        // Given
        EstacionFavoritaRequest request = new EstacionFavoritaRequest();
        request.setEstacionIdExterno("station123");
        request.setNombre("Zurich Hauptbahnhof");

        when(usuarioRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUsuario));
        when(estacionFavoritaRepository.existsByUsuarioIdAndEstacionIdExterno(anyLong(), anyString())).thenReturn(true);

        // When & Then
        assertThrows(EstacionYaExisteException.class, () -> {
            estacionFavoritaService.crearEstacionFavorita(request);
        });
        
        verify(usuarioRepository).findByEmail("john@example.com");
        verify(estacionFavoritaRepository).existsByUsuarioIdAndEstacionIdExterno(1L, "station123");
        verify(estacionFavoritaRepository, never()).save(any(EstacionFavorita.class));
    }

    @Test
    @DisplayName("obtenerEstacionesFavoritas - Debe devolver lista vacía cuando no hay estaciones favoritas")
    void obtenerEstacionesFavoritas_EmptyList() {
        // Given
        when(usuarioRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUsuario));
        when(estacionFavoritaRepository.findByUsuarioIdOrderByCreatedAtDesc(anyLong())).thenReturn(Arrays.asList());

        // When
        List<EstacionFavoritaResponse> result = estacionFavoritaService.obtenerEstacionesFavoritas();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(usuarioRepository).findByEmail("john@example.com");
        verify(estacionFavoritaRepository).findByUsuarioIdOrderByCreatedAtDesc(1L);
    }

    @Test
    @DisplayName("obtenerEstacionesFavoritas - Debe devolver lista con elementos")
    void obtenerEstacionesFavoritas_WithElements() {
        // Given
        when(usuarioRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUsuario));
        
        EstacionFavorita estacion1 = new EstacionFavorita();
        estacion1.setId(1L);
        estacion1.setEstacionIdExterno("station123");
        estacion1.setNombreEstacion("Zurich Hauptbahnhof");
        estacion1.setUsuario(mockUsuario);
        estacion1.setUsuarioId(1L);
        estacion1.setCreatedAt(LocalDateTime.now().minusDays(1));
        
        EstacionFavorita estacion2 = new EstacionFavorita();
        estacion2.setId(2L);
        estacion2.setEstacionIdExterno("station456");
        estacion2.setNombreEstacion("Bern Bahnhof");
        estacion2.setUsuario(mockUsuario);
        estacion2.setUsuarioId(1L);
        estacion2.setCreatedAt(LocalDateTime.now());
        
        when(estacionFavoritaRepository.findByUsuarioIdOrderByCreatedAtDesc(anyLong()))
                .thenReturn(Arrays.asList(estacion1, estacion2));

        // When
        List<EstacionFavoritaResponse> result = estacionFavoritaService.obtenerEstacionesFavoritas();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("station123", result.get(0).getEstacionIdExterno());
        assertEquals("Zurich Hauptbahnhof", result.get(0).getNombre());
        assertEquals("station456", result.get(1).getEstacionIdExterno());
        assertEquals("Bern Bahnhof", result.get(1).getNombre());
        verify(usuarioRepository).findByEmail("john@example.com");
        verify(estacionFavoritaRepository).findByUsuarioIdOrderByCreatedAtDesc(1L);
    }

    @Test
    @DisplayName("eliminarEstacionFavorita - Debe eliminar una estación favorita exitosamente")
    void eliminarEstacionFavorita_Success() {
        // Given
        Long estacionId = 1L;
        EstacionFavorita estacion = new EstacionFavorita();
        estacion.setId(estacionId);
        estacion.setEstacionIdExterno("station123");
        estacion.setNombreEstacion("Zurich Hauptbahnhof");
        estacion.setUsuario(mockUsuario);
        estacion.setUsuarioId(1L);
        estacion.setCreatedAt(LocalDateTime.now());

        when(usuarioRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUsuario));
        when(estacionFavoritaRepository.findById(estacionId)).thenReturn(Optional.of(estacion));

        // When
        estacionFavoritaService.eliminarEstacionFavorita(estacionId);

        // Then
        verify(usuarioRepository).findByEmail("john@example.com");
        verify(estacionFavoritaRepository).findById(estacionId);
        verify(estacionFavoritaRepository).delete(estacion);
    }

    @Test
    @DisplayName("eliminarEstacionFavorita - Debe lanzar EstacionNoEncontradaException cuando la estación no existe")
    void eliminarEstacionFavorita_NotFound_ThrowsException() {
        // Given
        Long estacionId = 1L;
        when(usuarioRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUsuario));
        when(estacionFavoritaRepository.findById(estacionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EstacionNoEncontradaException.class, () -> {
            estacionFavoritaService.eliminarEstacionFavorita(estacionId);
        });
        
        verify(usuarioRepository).findByEmail("john@example.com");
        verify(estacionFavoritaRepository).findById(estacionId);
        verify(estacionFavoritaRepository, never()).delete(any(EstacionFavorita.class));
    }

    @Test
    @DisplayName("eliminarEstacionFavorita - Debe lanzar EstacionNoEncontradaException cuando el usuario no tiene permisos")
    void eliminarEstacionFavorita_Unauthorized_ThrowsException() {
        // Given
        Long estacionId = 1L;
        Usuario otroUsuario = new Usuario(2L, "Jane Smith", "jane@example.com", "password456", "Geneva", LocalDateTime.now());
        
        EstacionFavorita estacion = new EstacionFavorita();
        estacion.setId(estacionId);
        estacion.setEstacionIdExterno("station123");
        estacion.setNombreEstacion("Zurich Hauptbahnhof");
        estacion.setUsuario(otroUsuario);
        estacion.setUsuarioId(2L);
        estacion.setCreatedAt(LocalDateTime.now());

        when(usuarioRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUsuario));
        when(estacionFavoritaRepository.findById(estacionId)).thenReturn(Optional.of(estacion));

        // When & Then
        assertThrows(EstacionNoEncontradaException.class, () -> {
            estacionFavoritaService.eliminarEstacionFavorita(estacionId);
        });
        
        verify(usuarioRepository).findByEmail("john@example.com");
        verify(estacionFavoritaRepository).findById(estacionId);
        verify(estacionFavoritaRepository, never()).delete(any(EstacionFavorita.class));
    }
}