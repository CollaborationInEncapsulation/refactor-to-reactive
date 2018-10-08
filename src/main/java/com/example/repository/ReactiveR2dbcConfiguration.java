package com.example.repository;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.function.TransactionalDatabaseClient;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;

@Configuration
public class ReactiveR2dbcConfiguration {

    @Bean
    public CommandLineRunner runScripts(
        DatabaseClient client,
        DataSourceProperties dataSourceProperties,
        ApplicationContext context
    ) {
        return args -> dataSourceProperties
            .getSchema()
            .stream()
            .map(context::getResource)
            .flatMap(r -> {
                try {
                    return Arrays.stream(
                        Files
                            .lines(Paths.get(r.getFile().getPath()))
                            .collect(Collectors.joining())
                            .split(";")
                    );
                }
                catch (IOException e) {
                    return Stream.empty();
                }
            })
            .forEach(line -> client.execute().sql(line).fetch().rowsUpdated().block());
    }

    @Bean
    MessageRepository messageRepository(R2dbcRepositoryFactory factory) {
        return factory.getRepository(MessageRepository.class);
    }

    @Bean
    UserRepository userRepository(R2dbcRepositoryFactory factory) {
        return factory.getRepository(UserRepository.class);
    }

    @Bean
    MentionRepository mentionRepository(R2dbcRepositoryFactory factory) {
        return factory.getRepository(MentionRepository.class);
    }

    @Bean
    R2dbcRepositoryFactory repositoryFactory(DatabaseClient client) {

        RelationalMappingContext context = new RelationalMappingContext();
        context.afterPropertiesSet();

        return new R2dbcRepositoryFactory(client, context);
    }

    @Bean
    TransactionalDatabaseClient databaseClient(ConnectionFactory factory) {

        return TransactionalDatabaseClient
                .builder()
                .connectionFactory(factory)
                .build();
    }

    @Bean
    PostgresqlConnectionFactory connectionFactory(
            DataSourceProperties properties
    ) {
        URI uri = URI.create(properties.determineUrl().replace("jdbc:", ""));

        PostgresqlConnectionConfiguration config = PostgresqlConnectionConfiguration
                .builder()
                .host(uri.getHost())
                .port(uri.getPort())
                .database(properties.determineDatabaseName())
                .username(properties.determineUsername())
                .password(properties.getPassword())
                .build();

        return new PostgresqlConnectionFactory(config);
    }
}
