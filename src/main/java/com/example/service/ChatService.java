package com.example.service;

import java.util.List;

public interface ChatService<T> {

	List<T> getLatestMessages();

	List<T> getMessagesAfter(String messageId);
}
