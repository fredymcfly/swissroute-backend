package com.swissroute.swissroute.service;

import com.swissroute.swissroute.exception.Http400Exception;
import com.swissroute.swissroute.exception.Http404Exception;
import com.swissroute.swissroute.exception.Http500Exception;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {

    private final WebClient webClient;

    public WebClientService(WebClient webClient) {
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

    public Mono<String> getLocations(String query) {
        return webClient
            .get()
            .uri(uriBuilder ->
                uriBuilder.path("/locations").queryParam("query", query).build()
            )
            .retrieve()
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
            .onStatus(
                org.springframework.http.HttpStatusCode::is5xxServerError,
                response ->
                    Mono.error(
                        new Http500Exception(
                            "Internal Server Error: " +
                                response.statusCode().value()
                        )
                    )
            )
            .bodyToMono(String.class);
    }
}
