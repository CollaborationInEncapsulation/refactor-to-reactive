package com.example.service.impl;

import java.util.Set;

import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.domain.Mention;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.repository.MentionRepository;
import com.example.repository.MessageRepository;
import com.example.repository.UserRepository;
import com.example.service.StatisticService;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.utils.MentionMapper;
import com.example.service.impl.utils.MessageMapper;
import com.example.service.impl.utils.UserMapper;
import io.r2dbc.postgresql.PostgresqlServerErrorException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultStatisticService implements StatisticService {
    private static final UserVM EMPTY_USER = new UserVM("", "");

    private final UserRepository    userRepository;
    private final MessageRepository messageRepository;
    private final MentionRepository mentionRepository;

    @Autowired
    public DefaultStatisticService(
        UserRepository userRepository,
        MessageRepository messageRepository,
        MentionRepository mentionRepository
    ) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.mentionRepository = mentionRepository;
    }

    @Override
    public Mono<UsersStatisticVM> updateStatistic(Iterable<MessageResponse> messages) {
        return Flux.fromIterable(messages)
                   .flatMap(this::saveMessage)
                   .then(this.doGetUserStatistic());
    }

    Mono<Message> saveMessage(MessageResponse messageResponse) {
        Message message = MessageMapper.toDomainUnit(messageResponse);
        Set<User> user = UserMapper.toDomainUnit(messageResponse);
        Set<Mention> mentions = MentionMapper.toDomainUnit(messageResponse);

        return userRepository
            .saveAll(user)
            .onErrorContinue(PostgresqlServerErrorException.class, (t, o) -> { })
            .then(
                messageRepository
                    .save(message)
                    .flux()
                    .onErrorContinue(PostgresqlServerErrorException.class, (t, o) -> { })
                    .then()
            )
            .then(
                mentionRepository
                    .saveAll(mentions)
                    .onErrorContinue(PostgresqlServerErrorException.class, (t, o) -> { })
                    .then()
            )
            .then(Mono.just(message));
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
