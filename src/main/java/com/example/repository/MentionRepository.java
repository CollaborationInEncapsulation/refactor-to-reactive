package com.example.repository;

import com.example.domain.Mention;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MentionRepository extends ReactiveCrudRepository<Mention, String> { }