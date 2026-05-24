package com.swissroute.swissroute.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissroute.swissroute.dto.RegistroRequest;
import com.swissroute.swissroute.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void registro_Success_ReturnsCreatedUsuarioResponse() throws Exception {
        // Given
        when(usuarioService.registro(any(RegistroRequest.class)))
                .thenReturn(new com.swissroute.swissroute.dto.UsuarioResponse(1L, "Juan Pérez", "juan.perez@example.com", "Zurich", null));

        // When & Then
        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
    }

    @Test
    void registro_InvalidEmail_ReturnsBadRequest() throws Exception {
        // Given
        validRequest.setEmail("invalid-email");

        // When & Then
        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registro_MissingFields_ReturnsBadRequest() throws Exception {
        // Given
        validRequest.setNombre(null);

        // When & Then
        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }
}