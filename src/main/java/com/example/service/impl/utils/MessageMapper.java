package com.example.service.impl.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.controller.vm.MessageVM;
import com.example.domain.Message;
import com.example.service.gitter.dto.MessageResponse;

public final class MessageMapper {
    private MessageMapper() {
    }

    public static List<MessageVM> toViewModelUnits(Iterable<MessageResponse> messages) {
        if (messages == null) {
            return null;
        }

        List<MessageVM> vms = new ArrayList<>();

        for (MessageResponse message : messages) {
            vms.add(toViewModelUnit(message));
        }

        return vms;

    }

    public static List<Message> toDomainUnits(Iterable<MessageResponse> messages) {
        if (messages == null) {
            return null;
        }

        List<Message> persistable = new ArrayList<>();

        for (MessageResponse message : messages) {
            persistable.add(toDomainUnit(message));
        }

        return persistable;
    }

    public static Message toDomainUnit(MessageResponse message) {
        if (message == null) {
            return null;
        }

        return Message.builder()
                      .id(message.getId())
                      .html(message.getHtml())
                      .text(message.getText())
                      .sent(message.getSent())
                      .readBy(message.getReadBy())
                      .userId(message.getFromUser().getId())
                      .build();
    }

    public static MessageVM toViewModelUnit(MessageResponse message) {
        if (message == null) {
            return null;
        }

        return new MessageVM(
                message.getId(),
                message.getText(),
                message.getHtml(),
                message.getFromUser().getUsername(),
                message.getFromUser().getAvatarUrl(),
                message.getSent()
        );
    }
}
