package com.swissroute.swissroute.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissroute.swissroute.dto.*;
import com.swissroute.swissroute.dto.external.ExternalConnectionResponse;
import com.swissroute.swissroute.dto.external.ExternalConnection;
import com.swissroute.swissroute.dto.external.ExternalSection;
import com.swissroute.swissroute.dto.external.ExternalStation;
import com.swissroute.swissroute.service.UsuarioService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FullIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    private String jwtToken;
    private String testUserEmail;
    private String testUserPassword = "password123";
    private static final AtomicLong counter = new AtomicLong(0);

    @BeforeEach
    public void setup() throws Exception {
        // Generate unique email for each test with timestamp 
        long timestamp = System.currentTimeMillis();
        long testCounter = counter.incrementAndGet();
        testUserEmail = "test" + timestamp + "_" + testCounter + "@example.com";
        
        // Ensure JWT is clean
        jwtToken = null;
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clean up user if exists (optional cleanup)
        try {
            // The @Transactional annotation will rollback changes
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("Tarea 2 - Registro de usuarios") 
    public void testUsuarioRegistro() throws Exception {
        // Setup request DTO
        RegistroRequest request = new RegistroRequest();
        request.setEmail(testUserEmail);
        request.setPassword(testUserPassword);
        request.setNombre("Integration Test User");

        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(testUserEmail))
                .andExpect(jsonPath("$.nombre").value("Integration Test User"));
    }

    @Test
    @DisplayName("Tarea 3 - Login y obtención de token JWT")
    public void testUsuarioLogin() throws Exception {
        // First register the user
        testUsuarioRegistro();

        // Then login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(testUserEmail);
        loginRequest.setPassword(testUserPassword);

        mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());

        // Store the token for later use
        MvcResult result = mockMvc.perform(post("/api/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();

        // Extract JWT token
        jwtToken = objectMapper.readTree(response).get("token").asText();
        
        // Verify that token was correctly set
        Assertions.assertNotNull(jwtToken, "JWT token should not be null");
    }

    @Test
    @DisplayName("Tarea 5 - Búsqueda de estaciones por nombre")
    public void testBuscarEstacionesPorNombre() throws Exception {
        // First login to get valid token
        testUsuarioLogin();

        // Mock external API response for station search by name
        mockExternalApiForStationSearch();

        mockMvc.perform(get("/api/estaciones")
                .param("query", "Zurich")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").exists())
                .andExpect(jsonPath("$[0].latitud").isNumber())
                .andExpect(jsonPath("$[0].longitud").isNumber());
    }

    @Test
    @DisplayName("Tarea 6 - Búsqueda de estaciones por coordenadas")
    public void testBuscarEstacionesPorCoordenadas() throws Exception {
        // First login to get valid token
        testUsuarioLogin();

        // Mock external API response for station search by coordinates 
        mockExternalApiForStationSearchByCoordinates();

        mockMvc.perform(get("/api/estaciones")
                .param("x", "47.376852")
                .param("y", "8.541659")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").exists())
                .andExpect(jsonPath("$[0].latitud").isNumber())
                .andExpect(jsonPath("$[0].longitud").isNumber());
    }

    @Test
    @DisplayName("Tarea 7 - Conexiones entre estaciones")
    public void testConexionesEntreEstaciones() throws Exception {
        // First login to get valid token
        testUsuarioLogin();

        // Mock external API response for connections
        mockExternalApiForConnections();

        mockMvc.perform(get("/api/conexiones")
                .param("from", "Zurich HB")
                .param("to", "Bern HB")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].origen").exists())
                .andExpect(jsonPath("$[0].destino").exists())
                .andExpect(jsonPath("$[0].secciones").isArray())
                .andExpect(jsonPath("$[0].productos").isArray());
    }

    @Test
    @DisplayName("Tarea 8 - Historial de búsquedas (con verificación automática)")
    public void testHistorialDeBusquedas() throws Exception {
        // First login to get valid token
        testUsuarioLogin();

        // Ensure there's no history yet by getting page 0
        mockMvc.perform(get("/api/historial")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));

        // Mock external API response for station search
        mockExternalApiForStationSearch();

        // First search connection - this should create a history entry
        mockMvc.perform(get("/api/conexiones")
                .param("from", "Zurich HB")
                .param("to", "Bern HB")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that history was created and contains the search
        mockMvc.perform(get("/api/historial")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].origen").exists())
                .andExpect(jsonPath("$.content[0].destino").exists())
                .andExpect(jsonPath("$.content[0].numResultados").isNumber());
    }



    // Helper method to mock external API responses for station searches
    private void mockExternalApiForStationSearch() throws Exception {
        // This would normally be handled by an external API mock
        // In a real test, you would mock these API calls
        // For now, we'll just validate the endpoint works as expected with valid parameters
    }

    // Helper method to mock external API responses for station searches by coordinates  
    private void mockExternalApiForStationSearchByCoordinates() throws Exception {
        // This would normally be handled by an external API mock
        // In a real test, you would mock these API calls 
        // For now, we'll just validate the endpoint works as expected with valid parameters
    }

    // Helper method to mock external API responses for connections
    private void mockExternalApiForConnections() throws Exception {
        // This would normally be handled by an external API mock
        // In a real test, you would mock these API calls
        // For now, we'll just validate the endpoint works as expected with valid parameters
    }
}