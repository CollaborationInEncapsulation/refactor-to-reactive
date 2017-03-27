package com.example.service.impl.utils;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.domain.Mention;
import com.example.service.gitter.dto.MessageResponse;

public final class MentionMapper {
    private MentionMapper() {
    }

    public static Set<Mention> toDomainUnit(MessageResponse messageResponse) {
        return messageResponse
                .getMentions()
                .stream()
                .filter(m -> m.getUserId() != null)
                .map(m -> Mention.of(
                        m.getUserId() + messageResponse.getId(),
                        m.getUserId(),
                        messageResponse.getId()
                ))
                .collect(Collectors.toSet());
    }
}
