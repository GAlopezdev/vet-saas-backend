package com.veterinaria.service.impl;

import com.veterinaria.dto.external.RucExternalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ConsultaRucService {

    private final WebClient webClient;

    @Value("${api.peru.token}")
    private String token;

    public ConsultaRucService(WebClient.Builder webClientBuilder,
                              @Value("${api.peru.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public RucExternalResponse buscarRuc(String ruc) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ruc/{ruc}")
                        .queryParam("token", token)
                        .build(ruc))
                .retrieve()
                .bodyToMono(RucExternalResponse.class)
                .block();
    }
}