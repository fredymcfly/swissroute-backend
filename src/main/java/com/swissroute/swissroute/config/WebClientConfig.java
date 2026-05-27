package com.swissroute.swissroute.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Value("${swiss.api.base-url:http://transport.opendata.ch/v1}")
    private String baseUrl;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(10))
            .compress(true);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(c -> c.defaultCodecs().maxInMemorySize(1024 * 1024))
            .build();

        return WebClient.builder()
            .baseUrl(baseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(exchangeStrategies)
            .build();
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
