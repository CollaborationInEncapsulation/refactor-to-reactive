package com.example.service.impl.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.example.controller.vm.UserVM;
import com.example.domain.User;
import com.example.service.gitter.dto.MessageResponse;
import com.example.service.gitter.dto.UserResponse;

public final class UserMapper {
    private UserMapper() {
    }

    public static Set<User> toDomainUnit(MessageResponse messageResponse) {
        UserResponse fromUser = messageResponse.getFromUser();
        ArrayList<User> users = new ArrayList<>();
        users.add(User.of(
                fromUser.getId(),
                fromUser.getDisplayName()
        ));

        messageResponse.getMentions()
                       .forEach(m -> {
                           if(m.getUserId() != null) {
                               users.add(User.of(m.getUserId(), m.getScreenName()));
                           }
                       });

        return new HashSet<>(users);
    }

    public static UserVM toViewModelUnits(User domainUser) {
        if (domainUser == null) {
            return null;
        }

        return new UserVM(domainUser.getId(), domainUser.getDisplayName());
    }
}
