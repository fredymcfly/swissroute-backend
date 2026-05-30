package com.swissroute.swissroute.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissroute.swissroute.dto.ConnectionDTO;
import com.swissroute.swissroute.dto.StationDTO;
import com.swissroute.swissroute.dto.external.ExternalConnectionResponse;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.exception.ExternalApiException;
import com.swissroute.swissroute.exception.Http400Exception;
import com.swissroute.swissroute.exception.Http404Exception;
import com.swissroute.swissroute.exception.Http500Exception;
import com.swissroute.swissroute.mapper.ConnectionMapper;
import com.swissroute.swissroute.repository.UsuarioRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TransportService {

    private final WebClient webClient;

    private final HistorialBusquedaService historialBusquedaService;
    
    private final UsuarioRepository usuarioRepository;
    
    public TransportService(WebClient webClient, HistorialBusquedaService historialBusquedaService, UsuarioRepository usuarioRepository) {
        this.webClient = webClient;
        this.historialBusquedaService = historialBusquedaService;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ConnectionDTO> getConnections(
            String from,
            String to,
            String date,
            String time,
            String transportations


    )
    {
        ExternalConnectionResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/connections")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .queryParamIfPresent("date", java.util.Optional.ofNullable(date))
                        .queryParamIfPresent("time", java.util.Optional.ofNullable(time))
                        .queryParamIfPresent("transportations", java.util.Optional.ofNullable(transportations))
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> {
                            throw new ExternalApiException("Error llamando a la API externa de conexiones");
                        }
                )
                .bodyToMono(ExternalConnectionResponse.class)
                .block();

        if (response == null || response.connections() == null) {
            throw new ExternalApiException("La API externa no devolvió conexiones");
        }

        List<ConnectionDTO> conexiones = response.connections()
                .stream()
                .map(ConnectionMapper::toDTO)
                .toList();

        Usuario usuario = obtenerUsuarioAutenticado();

        historialBusquedaService.guardarBusqueda(
                 from,
                 to,
                 conexiones.size(),
                 usuario
         );


        return conexiones;
    }

    public List<StationDTO> getLocations(String query) {

        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        return webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/locations")
                                .queryParam("query", query)
                                .build()
                )
                .retrieve()


                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> {
                            int code = response.statusCode().value();

                            if (code == 404) {
                                return Mono.error(
                                        new Http404Exception("Not Found: " + code)
                                );
                            }

                            return Mono.error(
                                    new Http400Exception("Bad Request: " + code)
                            );
                        }
                )


                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                new Http500Exception(
                                        "Servicio externo no disponible (503)"
                                )
                        )
                )


                .bodyToMono(String.class)
                .map(this::mapToStations).block();
    }

    private List<StationDTO> mapToStations(String json) {

        List<StationDTO> result = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode stations = root.path("stations");

            for (JsonNode node : stations) {

                StationDTO dto = new StationDTO();

                dto.setId(node.path("id").asText());
                dto.setNombre(node.path("name").asText());


                dto.setLongitud(node.path("coordinate").path("x").asDouble());
                dto.setLatitud(node.path("coordinate").path("y").asDouble());

                result.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error parsing API response", e);
        }

        return result;
    }

    public List<StationDTO> getLocationsByCoordinates(double x, double y) {
        return webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/locations")
                                .queryParam("x", x)
                                .queryParam("y", y)
                                .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    int code = response.statusCode().value();

                    if (code == 404) {
                        return Mono.error(new Http404Exception("Not Found: " + code));
                    }

                    return Mono.error(new Http400Exception("Bad Request: " + code));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new Http500Exception("Servicio externo no disponible (503)"))
                )
                .bodyToMono(String.class)
                .map(this::mapToStations).block();
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay usuario autenticado");
        }
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
