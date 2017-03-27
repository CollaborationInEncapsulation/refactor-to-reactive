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
    public UsersStatisticVM updateStatistic(Iterable<MessageResponse> messages) {
        messages.forEach(this::saveMessage);

        return doGetUserStatistic();
    }

    void saveMessage(MessageResponse messageResponse) {
        Message message = MessageMapper.toDomainUnit(messageResponse);
        Set<User> users = UserMapper.toDomainUnit(messageResponse);
        Set<Mention> mentions = MentionMapper.toDomainUnit(messageResponse);

        for (User user : users) {
            try {
                userRepository.save(user);
            } catch (Exception e) {}
        }

        try {
            messageRepository.save(message);
        } catch (Exception e) {}

        for (Mention mention : mentions) {
            try {
                mentionRepository.save(mention);
            } catch (Exception e) {}
        }
    }

    private UsersStatisticVM doGetUserStatistic() {
        UserVM topActiveUser = userRepository.findMostActive()
                                             .map(UserMapper::toViewModelUnits)
                                             .orElse(EMPTY_USER);

        UserVM topMentionedUser = userRepository.findMostPopular()
                                                .map(UserMapper::toViewModelUnits)
                                                .orElse(EMPTY_USER);

        return new UsersStatisticVM(topActiveUser, topMentionedUser);
    }
}
