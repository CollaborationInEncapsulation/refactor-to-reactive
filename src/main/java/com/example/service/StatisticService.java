package com.example.service;

import com.example.controller.vm.UsersStatisticVM;
import com.example.service.gitter.dto.MessageResponse;
import reactor.core.publisher.Mono;

public interface StatisticService {

	Mono<UsersStatisticVM> updateStatistic(Iterable<MessageResponse> messages);
}
