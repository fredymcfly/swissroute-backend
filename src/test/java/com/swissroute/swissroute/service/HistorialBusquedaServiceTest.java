package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.HistorialBusquedaDTO;
import com.swissroute.swissroute.entity.HistorialBusqueda;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.repository.HistorialBusquedaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialBusquedaServiceTest {

    @Mock
    private HistorialBusquedaRepository historialBusquedaRepository;

    @InjectMocks
    private HistorialBusquedaService historialBusquedaService;

    @Captor
    private ArgumentCaptor<HistorialBusqueda> historialBusquedaCaptor;

    @Test
    @DisplayName("testGuardarBusqueda - Verify that a search is saved correctly with all fields")
    void testGuardarBusqueda() {
        // Given
        Usuario usuario = new Usuario(1L, "John Doe", "john@example.com", "password123", "Zurich", LocalDateTime.now());
        String origen = "Zurich";
        String destino = "Bern";
        int numResultados = 5;

        // When
        historialBusquedaService.guardarBusqueda(origen, destino, numResultados, usuario);

        // Then
        verify(historialBusquedaRepository).save(historialBusquedaCaptor.capture());
        HistorialBusqueda capturedHistorial = historialBusquedaCaptor.getValue();

        assertNotNull(capturedHistorial);
        assertEquals(origen, capturedHistorial.getOrigen());
        assertEquals(destino, capturedHistorial.getDestino());
        assertEquals(numResultados, capturedHistorial.getNumResultados());
        assertEquals(usuario, capturedHistorial.getUsuario());
        assertNotNull(capturedHistorial.getFechaConsulta());
    }

    @Test
    @DisplayName("testObtenerHistorial - Verify pagination and filtering by user")
    void testObtenerHistorial() {
        // Given
        Usuario usuario = new Usuario(1L, "John Doe", "john@example.com", "password123", "Zurich", LocalDateTime.now());
        Pageable pageable = PageRequest.of(0, 10);
        
        HistorialBusqueda historial1 = new HistorialBusqueda("Zurich", "Bern", LocalDateTime.now(), 5, usuario);
        HistorialBusqueda historial2 = new HistorialBusqueda("Bern", "Lucerne", LocalDateTime.now().plusDays(1), 3, usuario);
        
        List<HistorialBusqueda> historiales = Arrays.asList(historial1, historial2);
        Page<HistorialBusqueda> historialPage = new PageImpl<>(historiales);
        
        when(historialBusquedaRepository.findByUsuario(usuario, pageable)).thenReturn(historialPage);

        // When
        Page<HistorialBusquedaDTO> result = historialBusquedaService.obtenerHistorial(usuario, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(historialBusquedaRepository).findByUsuario(usuario, pageable);
        
        // Verify DTO contents
        HistorialBusquedaDTO dto1 = result.getContent().get(0);
        assertEquals(historial1.getId(), dto1.id());
        assertEquals(historial1.getOrigen(), dto1.origen());
        assertEquals(historial1.getDestino(), dto1.destino());
        assertEquals(historial1.getNumResultados(), dto1.numResultados());
    }

    @Test
    @DisplayName("testEliminarEntrada - Verify deletion by ID with user validation")
    void testEliminarEntrada() {
        // Given
        Usuario usuario = new Usuario(1L, "John Doe", "john@example.com", "password123", "Zurich", LocalDateTime.now());
        Long id = 1L;
        HistorialBusqueda historial = new HistorialBusqueda("Zurich", "Bern", LocalDateTime.now(), 5, usuario);
        
        // Set id using reflection
        try {
            Field idField = HistorialBusqueda.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(historial, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        
        when(historialBusquedaRepository.findFirstByIdAndUsuarioId(eq(id), eq(usuario.getId())))
            .thenReturn(historial);

        // When
        historialBusquedaService.eliminarEntrada(id, usuario);

        // Then
        verify(historialBusquedaRepository).findFirstByIdAndUsuarioId(eq(id), eq(usuario.getId()));
        verify(historialBusquedaRepository).deleteById(id);
    }

    @Test
    @DisplayName("testEliminarTodo - Verify bulk deletion for a user")
    void testEliminarTodo() {
        // Given
        Usuario usuario = new Usuario(1L, "John Doe", "john@example.com", "password123", "Zurich", LocalDateTime.now());

        // When
        historialBusquedaService.eliminarTodo(usuario);

        // Then
        verify(historialBusquedaRepository).deleteByUsuario(usuario);
    }

    @Test
    @DisplayName("testEliminarEntrada_DeOtroUsuario - Verify cannot delete another user's entry (should throw exception)")
    void testEliminarEntrada_DeOtroUsuario() {
        // Given
        Usuario usuario1 = new Usuario(1L, "John Doe", "john@example.com", "password123", "Zurich", LocalDateTime.now());
        Usuario usuario2 = new Usuario(2L, "Jane Smith", "jane@example.com", "password456", "Geneva", LocalDateTime.now());
        Long id = 1L;
        
        // La entrada no belongs a usuario2, así que findFirstByIdAndUsuarioId devuelve null
        when(historialBusquedaRepository.findFirstByIdAndUsuarioId(eq(id), eq(usuario2.getId())))
            .thenReturn(null);

        // When & Then - Debería lanzar una excepción
        assertThrows(RuntimeException.class, () -> {
            historialBusquedaService.eliminarEntrada(id, usuario2);
        });
        
        verify(historialBusquedaRepository).findFirstByIdAndUsuarioId(eq(id), eq(usuario2.getId()));
    }
}