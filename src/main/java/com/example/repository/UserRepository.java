package com.example.repository;

import com.example.domain.User;

import reactor.core.publisher.Mono;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

    @Query(
        value =
            "SELECT CAST(users.id as VARCHAR(60)), CAST(users.display_name as VARCHAR(256)) " +
            "FROM users " +
                "INNER JOIN " +
                    "(" +
                        "SELECT user_id, count(user_id) AS activity " +
                        "FROM messages " +
                        "GROUP BY messages.user_id" +
                    ") as grouped_messages " +
                "ON users.id = grouped_messages.user_id " +
            "ORDER BY activity DESC " +
            "LIMIT 1"
    )
    Mono<User> findMostActive();


    @Query(
        value =
            "SELECT CAST(users.id as VARCHAR(60)), CAST(users.display_name as VARCHAR(256)) " +
            "FROM users " +
                "INNER JOIN " +
                    "(" +
                        "SELECT user_id, count(user_id) AS popularity " +
                        "FROM mentions " +
                        "GROUP BY mentions.user_id" +
                    ") as grouped_mentions " +
                "ON users.id = grouped_mentions.user_id " +
            "ORDER BY popularity DESC " +
            "LIMIT 1"
    )
    Mono<User> findMostPopular();
}
