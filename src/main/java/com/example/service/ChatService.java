package com.example.service;

import java.util.List;

public interface ChatService<T> {

	List<T> getMessagesAfter(String messageId);
}
