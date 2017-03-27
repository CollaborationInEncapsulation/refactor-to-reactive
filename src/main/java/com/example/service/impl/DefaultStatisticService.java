package com.example.service.impl;

import com.example.controller.vm.UserVM;
import com.example.controller.vm.UsersStatisticVM;
import com.example.repository.MessageRepository;
import com.example.repository.UserRepository;
import com.example.service.StatisticService;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.impl.utils.UserMapper;
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
    public UsersStatisticVM updateStatistic(Iterable<MessageResponse> messages) {
        messageRepository.save(toDomainUnits(messages));

        return doGetUserStatistic();
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
