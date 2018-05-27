package com.example.service.impl;

import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.repository.MessageRepository;
import com.example.repository.UserRepository;
import com.example.service.StatisticService;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.utils.UserMapper;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.service.impl.utils.MessageMapper.toDomainUnits;

@Service
public class DefaultStatisticService implements StatisticService {
    private static final UserVM EMPTY_USER = new UserVM("", "");

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public DefaultStatisticService(UserRepository userRepository,
            MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Mono<UsersStatisticVM> updateStatistic(Iterable<MessageResponse> messages) {
        return messageRepository.saveAll(toDomainUnits(messages))
                                .then(doGetUserStatistic());
    }

    private Mono<UsersStatisticVM> doGetUserStatistic() {
        Mono<UserVM> topActiveUserMono = userRepository.findMostActive()
                                                       .map(UserMapper::toViewModelUnits)
                                                       .defaultIfEmpty(EMPTY_USER);

        Mono<UserVM> topMentionedUserMono = userRepository.findMostPopular()
                                                          .map(UserMapper::toViewModelUnits)
                                                          .defaultIfEmpty(EMPTY_USER);

        return Mono.zip(topActiveUserMono, topMentionedUserMono, UsersStatisticVM::new);
    }
}
