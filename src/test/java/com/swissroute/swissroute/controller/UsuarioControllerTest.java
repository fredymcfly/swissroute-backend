package com.swissroute.swissroute.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissroute.swissroute.config.JwtAuthenticationFilter;
import com.swissroute.swissroute.dto.RegistroRequest;
import com.swissroute.swissroute.dto.UsuarioResponse;
import com.swissroute.swissroute.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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
        when(usuarioService.registro(any(RegistroRequest.class))).thenReturn(
            new UsuarioResponse(
                1L,
                "Juan Pérez",
                "juan.perez@example.com",
                "Zurich",
                null
            )
        );

        mockMvc
            .perform(
                post("/api/usuarios/registro")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
            .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
    }

    @Test
    void registro_InvalidEmail_ReturnsBadRequest() throws Exception {
        validRequest.setEmail("invalid-email");

        mockMvc
            .perform(
                post("/api/usuarios/registro")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void registro_MissingFields_ReturnsBadRequest() throws Exception {
        validRequest.setNombre(null);

        mockMvc
            .perform(
                post("/api/usuarios/registro")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest))
            )
            .andExpect(status().isBadRequest());
    }
}
