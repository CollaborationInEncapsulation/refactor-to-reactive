package com.example.controller;

import java.util.function.Function;

import com.example.controller.vm.MessageVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.service.ChatService;
import com.example.service.StatisticService;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.utils.MessageMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/info")
public class InfoResource {

	private final ReplayProcessor<UsersStatisticVM> statisticStream = ReplayProcessor.cacheLast();
	private final ReplayProcessor<MessageVM>        messagesStream  = ReplayProcessor.create(50);

    @Autowired
    public InfoResource(ChatService<MessageResponse> chatService,
		    StatisticService statisticService) {
	    Flux.mergeSequential(
	            chatService.getMessagesAfter("")
	                       .flatMapIterable(Function.identity()),
			    chatService.getMessagesStream()
	        )
	        .publish(flux -> Flux.merge(
                flux.map(MessageMapper::toViewModelUnit)
                    .subscribeWith(messagesStream),
		        flux.transform(statisticService::updateStatistic)
		            .subscribeWith(statisticStream)
	        ))
	        .subscribe();
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> stream() {
	    return Flux.merge(messagesStream, statisticStream);
    }
}
