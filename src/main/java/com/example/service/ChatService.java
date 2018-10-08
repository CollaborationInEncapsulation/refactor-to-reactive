package com.example.service;

import java.util.List;

import reactor.core.publisher.Mono;

public interface ChatService<T> {

	Mono<List<T>> getMessagesAfter(String messageId);
}
