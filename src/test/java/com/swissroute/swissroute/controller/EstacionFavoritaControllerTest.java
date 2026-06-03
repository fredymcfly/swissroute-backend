package com.swissroute.swissroute.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissroute.swissroute.config.JwtAuthenticationFilter;
import com.swissroute.swissroute.dto.EstacionFavoritaRequest;
import com.swissroute.swissroute.dto.EstacionFavoritaResponse;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.exception.EstacionNoEncontradaException;
import com.swissroute.swissroute.exception.EstacionYaExisteException;
import com.swissroute.swissroute.repository.UsuarioRepository;
import com.swissroute.swissroute.service.EstacionFavoritaService;
import com.swissroute.swissroute.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EstacionFavoritaController.class)
@AutoConfigureMockMvc(addFilters = false)
class EstacionFavoritaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstacionFavoritaService estacionFavoritaService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private EstacionFavoritaRequest validRequest;
    private EstacionFavoritaResponse response1;
    private EstacionFavoritaResponse response2;

    @BeforeEach
    void setUp() {
        validRequest = new EstacionFavoritaRequest();
        validRequest.setEstacionIdExterno("station123");
        validRequest.setNombre("Zurich Hauptbahnhof");

        response1 = new EstacionFavoritaResponse();
        response1.setId(1L);
        response1.setEstacionIdExterno("station123");
        response1.setNombre("Zurich Hauptbahnhof");
        response1.setCreatedAt(LocalDateTime.now());

        response2 = new EstacionFavoritaResponse();
        response2.setId(2L);
        response2.setEstacionIdExterno("station456");
        response2.setNombre("Bern Bahnhof");
        response2.setCreatedAt(LocalDateTime.now().plusDays(1));
    }

    @Test
    void crearEstacionFavorita_Success_ReturnsCreated() throws Exception {
        when(estacionFavoritaService.crearEstacionFavorita(any(EstacionFavoritaRequest.class))).thenReturn(response1);

        mockMvc
            .perform(
                post("/api/estaciones-favoritas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.estacionIdExterno").value("station123"))
            .andExpect(jsonPath("$.nombre").value("Zurich Hauptbahnhof"));
    }

//    @Test
//    void crearEstacionFavorita_Duplicate_ThrowsException_ReturnsConflict() throws Exception {
//        when(estacionFavoritaService.crearEstacionFavorita(any(EstacionFavoritaRequest.class)))
//            .thenThrow(new EstacionYaExisteException("station123"));
//
//        mockMvc
//            .perform(
//                post("/api/estaciones-favoritas")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(validRequest))
//            )
//            .andExpect(status().isConflict());
//    }

    @Test
    void obtenerEstacionesFavoritas_Success_ReturnsOk() throws Exception {
        List<EstacionFavoritaResponse> responses = Arrays.asList(response1, response2);
        when(estacionFavoritaService.obtenerEstacionesFavoritas()).thenReturn(responses);

        mockMvc
            .perform(
                get("/api/estaciones-favoritas")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].estacionIdExterno").value("station123"))
            .andExpect(jsonPath("$[0].nombre").value("Zurich Hauptbahnhof"))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].estacionIdExterno").value("station456"))
            .andExpect(jsonPath("$[1].nombre").value("Bern Bahnhof"));
    }

    @Test
    void eliminarEstacionFavorita_Success_ReturnsNoContent() throws Exception {
        mockMvc
            .perform(
                delete("/api/estaciones-favoritas/1")
            )
            .andExpect(status().isNoContent());
            
        verify(estacionFavoritaService).eliminarEstacionFavorita(1L);
    }

//    @Test
//    void eliminarEstacionFavorita_NotFound_ThrowsException_ReturnsNotFound() throws Exception {
//        doThrow(new EstacionNoEncontradaException(1L)).when(estacionFavoritaService).eliminarEstacionFavorita(1L);
//
//        mockMvc
//            .perform(
//                delete("/api/estaciones-favoritas/1")
//            )
//            .andExpect(status().isNotFound());
//    }
}