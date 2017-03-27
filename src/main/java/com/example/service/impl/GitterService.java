package com.example.service.impl;

import java.util.List;

import com.example.service.ChatService;
import com.example.service.gitter.GitterProperties;
import com.example.service.gitter.GitterUriBuilder;
import com.example.service.gitter.dto.MessageResponse;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

@Service
@EnableConfigurationProperties(GitterProperties.class)
public class GitterService implements ChatService<MessageResponse> {

    private final RestTemplate restTemplate;
    private final GitterProperties gitterProperties;

    @Autowired
    public GitterService(RestTemplateBuilder builder, GitterProperties gitterProperties) {
        this.restTemplate = builder.build();
        this.gitterProperties = gitterProperties;
    }

    @Override
    @SneakyThrows
    public List<MessageResponse> getMessagesAfter(String messageId) {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();

        if (messageId != null) {
            query.add("afterId", messageId);
        }

        ResponseEntity<List<MessageResponse>> response = restTemplate.exchange(
                GitterUriBuilder.from(gitterProperties.getApi())
                                .queryParams(query)
                                .build()
                                .toUri(),
                HttpMethod.GET,
                new HttpEntity<>(WebUtils.parseMatrixVariables(
                        "Authorization=Bearer " + gitterProperties.getAuth().getToken()
                )),
                new ParameterizedTypeReference<List<MessageResponse>>() {
                }
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException(response.getBody().toString());
        }
    }
}
