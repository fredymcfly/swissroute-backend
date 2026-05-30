package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.StationBoardDTO;
import com.swissroute.swissroute.dto.external.ExternalStationBoardResponse;
import com.swissroute.swissroute.mapper.StationBoardMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;


import java.util.List;
import java.util.Optional;

@Service
public class StationBoardServiceImpl {


    private final WebClient webClient;

    public StationBoardServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<StationBoardDTO>> getStationboard(
            String station,
            int limit,
            List<String> transport
    ) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("transport.opendata.ch")
                        .path("/v1/stationboard")
                        .queryParam("station", station)
                        .queryParam("limit", limit)
                        .queryParamIfPresent("transportations[]", Optional.ofNullable(transport))
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        resp -> Mono.error(new RuntimeException("Error 4xx consultando la API externa")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> Mono.error(new RuntimeException("Error 5xx en el servidor externo")))
                .bodyToMono(ExternalStationBoardResponse.class)
                .map(response ->
                        response.stationboard().stream()
                                .map(StationBoardMapper::fromExternal)
                                .toList()
                );
    }
}