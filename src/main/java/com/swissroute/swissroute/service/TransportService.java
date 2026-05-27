package com.swissroute.swissroute.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swissroute.swissroute.dto.StationDTO;
import com.swissroute.swissroute.exception.Http400Exception;
import com.swissroute.swissroute.exception.Http404Exception;
import com.swissroute.swissroute.exception.Http500Exception;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransportService {

    private final WebClient webClient;

    public TransportService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getConnections(String from, String to) {
        return webClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .path("/connections")
                    .queryParam("from", from)
                    .queryParam("to", to)
                    .build()
            )
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> {
                int code = response.statusCode().value();

                if (code == 404) {
                    return Mono.error(
                        new Http404Exception("Not Found: " + code)
                    );
                }

                return Mono.error(new Http400Exception("Bad Request: " + code));
            })
            .onStatus(HttpStatusCode::is5xxServerError, response ->
                Mono.error(
                    new Http500Exception(
                        "Internal Server Error: " +
                            response.statusCode().value()
                    )
                )
            )
            .bodyToMono(String.class);
    }

    public Mono<List<StationDTO>> getLocations(String query) {

        if (query == null || query.isBlank()) {
            return Mono.error(
                    new Http400Exception("El parámetro 'query' es obligatorio")
            );
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

                // 🔴 4xx (errores cliente de API externa)
                .onStatus(
                        org.springframework.http.HttpStatusCode::is4xxClientError,
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

                // 🔴 5xx (fallo API externa → lo que pide la tarea)
                .onStatus(
                        org.springframework.http.HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                new Http500Exception(
                                        "Servicio externo no disponible (503)"
                                )
                        )
                )

                // 📦 respuesta cruda → DTO
                .bodyToMono(String.class)
                .map(this::mapToStations);
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

    public Mono<List<StationDTO>> getLocationsByCoordinates(double x, double y) {
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
                .map(this::mapToStations);
    }
}
