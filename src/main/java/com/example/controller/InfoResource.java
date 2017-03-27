package com.example.controller;

import java.util.ArrayList;
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
		    StatisticService statisticService) {
        this.chatService = chatService;
	    this.statisticService = statisticService;
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(value = "cursor", defaultValue = "") String cursor) {
        try {
            return pullAndRespond(cursor);
        }
        catch (Exception e) {
            return ResponseEntity.noContent()
                                 .header("cursor", cursor)
                                 .build();
        }
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<?> pullAndRespond(String cursor) {
        List<MessageResponse> messages = chatService.getMessagesAfter(cursor);

        if (!messages.isEmpty()) {
            String nextCursor = messages.get(messages.size() - 1)
                                        .getId();
            UsersStatisticVM statistic = statisticService.updateStatistic(messages);

            List response = new ArrayList<>(toViewModelUnits(messages));
            response.add(statistic);

            return ResponseEntity.ok()
                                 .header("cursor", nextCursor)
                                 .body(response);
        }
        else {
            return ResponseEntity.noContent()
                                 .header("cursor", cursor)
                                 .build();
        }
    }
}
