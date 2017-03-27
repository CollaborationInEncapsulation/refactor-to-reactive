package com.example.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.controller.vm.UsersStatisticVM;
import com.example.service.ChatService;
import com.example.service.StatisticService;
import com.example.service.gitter.dto.MessageResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.example.service.impl.utils.MessageMapper.toViewModelUnits;

@RestController
@RequestMapping("/api/v1/info")
public class InfoResource {

	private final ChatService<MessageResponse> chatService;
	private final StatisticService             statisticService;

    @Autowired
    public InfoResource(ChatService<MessageResponse> chatService,
		    StatisticService service) {
        this.chatService = chatService;
	    this.statisticService = service;
    }

    @GetMapping
    public ResponseEntity<List> list(@RequestParam(value = "cursor", required = false) String cursor) {
        List<MessageResponse> messages = chatService.getMessagesAfter(cursor);
        UsersStatisticVM statistic = statisticService.updateStatistic(messages);
        List response = new ArrayList<>();

        response.addAll(toViewModelUnits(messages));
        response.add(statistic);

        if (messages != null && messages.size() > 0) {
            return ResponseEntity.ok()
                    .header("cursor", messages.get(messages.size() - 1).getId())
                    .body(response);
        } else {
            ResponseEntity.HeadersBuilder<?> headersBuilder = ResponseEntity.noContent();

            if (cursor != null) {
                headersBuilder.header("cursor", cursor);
            }

            return headersBuilder.build();
        }
    }

    @ResponseStatus
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> fallback(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Object() {
            public String message = e.getMessage();
        });
    }
}
