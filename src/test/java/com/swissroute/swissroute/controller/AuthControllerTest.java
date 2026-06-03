package com.swissroute.swissroute.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissroute.swissroute.dto.LoginRequest;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.repository.EstacionFavoritaRepository;
import com.swissroute.swissroute.repository.HistorialBusquedaRepository;
import com.swissroute.swissroute.repository.RutaFavoritaRepository;
import com.swissroute.swissroute.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private HistorialBusquedaRepository historialBusquedaRepository;

    @Autowired
    private RutaFavoritaRepository rutaFavoritaRepository;

    @Autowired
    private EstacionFavoritaRepository estacionFavoritaRepository;

    @BeforeEach
    void setUp() {
        // Clean up test data in correct order to respect FK constraints:
        // historial_busquedas -> rutas_favoritas -> estaciones_favoritas -> usuarios
        historialBusquedaRepository.deleteAll();
        rutaFavoritaRepository.deleteAll();
        estacionFavoritaRepository.deleteAll();
        usuarioRepository.deleteAll();
        
        // Create a test user
        Usuario user = new Usuario();
        user.setNombre("Test User");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setCiudadBase("Zurich");
        usuarioRepository.save(user);
    }

    @Test
    void loginWithValidCredentialsShouldReturnToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void loginWithInvalidCredentialsShouldReturnUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginWithNonExistentUserShouldReturnUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}