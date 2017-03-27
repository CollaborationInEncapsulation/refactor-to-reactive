package com.example.controller;

import java.util.List;

import com.example.controller.vm.UsersStatisticVM;
import com.example.service.ChatService;
import com.example.service.StatisticService;
import com.example.service.gitter.dto.MessageResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.service.impl.utils.MessageMapper.toViewModelUnits;

@Controller
@RequestMapping
public class ChatController {

    private final ChatService<MessageResponse> chatService;
    private final StatisticService             statisticService;

    @Autowired
    public ChatController(ChatService<MessageResponse> chatService,
            StatisticService statisticService) {
        this.chatService = chatService;
        this.statisticService = statisticService;
    }

    @GetMapping
    public String index(Model model) {
        List<MessageResponse> messages = chatService.getMessagesAfter("");
        UsersStatisticVM statistic = statisticService.updateStatistic(messages);

        model.addAttribute("messages", toViewModelUnits(messages));
        model.addAttribute("statistic", statistic);

        if (messages != null && messages.size() > 0) {
            model.addAttribute("cursor", messages.get(messages.size() - 1).getId());
        }

        return "chat";
    }
}
