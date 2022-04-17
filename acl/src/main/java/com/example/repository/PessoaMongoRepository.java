package com.example.repository;

import com.example.model.PessoaMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaMongoRepository extends MongoRepository<PessoaMongo, String> {


}

