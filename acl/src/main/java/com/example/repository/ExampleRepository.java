package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Example;


@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {

    public Example findByName(String name);
}

