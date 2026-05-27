package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.ConnectionDTO;
import com.swissroute.swissroute.dto.external.ExternalConnectionResponse;
import com.swissroute.swissroute.exception.ExternalApiException;
import com.swissroute.swissroute.mapper.ConnectionMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ConnectionService {

    private final WebClient webClient;

    public ConnectionService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://transport.opendata.ch/v1")
                .build();
    }

    public List<ConnectionDTO> getConnections(
            String from,
            String to,
            String date,
            String time,
            String transportations
    ) {
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
                        status -> status.isError(),
                        clientResponse -> {
                            throw new ExternalApiException("Error llamando a la API externa de conexiones");
                        }
                )
                .bodyToMono(ExternalConnectionResponse.class)
                .block();

        if (response == null || response.connections() == null) {
            throw new ExternalApiException("La API externa no devolvió conexiones");
        }

        return response.connections()
                .stream()
                .map(ConnectionMapper::toDTO)
                .toList();
    }
}