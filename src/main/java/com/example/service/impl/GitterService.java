package com.example.service.impl;

import java.util.List;
import reactor.core.publisher.Mono;
import com.example.service.ChatService;
import com.example.service.gitter.GitterProperties;
import com.example.service.gitter.GitterUriBuilder;
import com.example.service.gitter.dto.MessageResponse;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@EnableConfigurationProperties(GitterProperties.class)
public class GitterService implements ChatService<MessageResponse> {

    private final WebClient        webClient;
    private final GitterProperties gitterProperties;

    @Autowired
    public GitterService(WebClient.Builder builder, GitterProperties gitterProperties) {
        this.webClient = builder
                            .defaultHeader("Authorization", "Bearer " + gitterProperties.getAuth().getToken())
                            .build();
        this.gitterProperties = gitterProperties;
    }

    @Override
    @SneakyThrows
    public Mono<List<MessageResponse>> getMessagesAfter(String messageId) {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();

        if (messageId != null) {
            query.add("afterId", messageId);
        }

        return webClient.get()
                        .uri(GitterUriBuilder.from(gitterProperties.getApi())
                                             .queryParams(query)
                                             .build()
                                             .toUri())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<MessageResponse>>() {});
    }
}
