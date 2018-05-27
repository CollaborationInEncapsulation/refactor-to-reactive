package com.example.controller;

import java.time.Duration;
import java.util.List;

import com.example.controller.vm.MessageVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.service.ChatService;
import com.example.service.StatisticService;
import com.example.service.gitter.dto.MessageResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.scheduler.Schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.service.impl.utils.MessageMapper.toViewModelUnits;

@RestController
@RequestMapping("/api/v1/info")
public class InfoResource {

	private final ChatService<MessageResponse> chatService;
	private final StatisticService             statisticService;

	private final ReplayProcessor<UsersStatisticVM> statisticStream = ReplayProcessor.cacheLast();
	private final ReplayProcessor<MessageVM>        messagesStream  = ReplayProcessor.create(50);
	private final FluxSink<UsersStatisticVM>        statisticSink   = statisticStream.sink();
	private final FluxSink<MessageVM>               messageSink     = messagesStream.sink();

    @Autowired
    public InfoResource(ChatService<MessageResponse> chatService,
		    StatisticService statisticService) {
        this.chatService = chatService;
	    this.statisticService = statisticService;

		ReplayProcessor<String> cursorSupplier = ReplayProcessor.cacheLast();
	    FluxSink<String> cursorSink = cursorSupplier.sink()
	                                                .next("");

	    cursorSupplier.delayElements(Duration.ofSeconds(1), Schedulers.elastic())
	                  .doOnNext(c -> pullAndRespond(c, cursorSink))
	                  .timeout(Duration.ofSeconds(3))
	                  .retryBackoff(Long.MAX_VALUE, Duration.ofMillis(200))
                      .subscribe();
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<?> stream() {
	    return Flux.merge(messagesStream, statisticStream);
    }

    @SuppressWarnings("unchecked")
    private void pullAndRespond(String cursor, FluxSink<String> cursorSink) {
        List<MessageResponse> messages = chatService.getMessagesAfter(cursor);

        if (!messages.isEmpty()) {
            String nextCursor = messages.get(messages.size() - 1)
                                        .getId();
            UsersStatisticVM statistic = statisticService.updateStatistic(messages);

			toViewModelUnits(messages).forEach(messageSink::next);
			statisticSink.next(statistic);

			cursorSink.next(nextCursor);
        }
        else {
			cursorSink.next(cursor);
        }
    }
}
