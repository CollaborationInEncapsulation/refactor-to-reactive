package com.example.repository;

import com.example.domain.Mention;

import org.springframework.data.repository.CrudRepository;

public interface MentionRepository extends CrudRepository<Mention, String> { }