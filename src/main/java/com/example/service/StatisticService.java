package com.example.service;

import com.example.controller.vm.UsersStatisticVM;
import com.example.service.gitter.dto.MessageResponse;

public interface StatisticService {

	UsersStatisticVM updateStatistic(Iterable<MessageResponse> messages);
}
