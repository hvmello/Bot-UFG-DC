package com.example.service;

import com.example.model.PessoaMongo;
import com.example.repository.PessoaMongoRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class PessoaMongoService {

    @Autowired
    private PessoaMongoRepository repository;

    @Autowired
    private AclService aclService;

    public PessoaMongo find(String id) throws NotFoundException {
        PessoaMongo example = repository.findById(id).get();
        aclService.hasPermission(example, Arrays.asList(BasePermission.READ,BasePermission.ADMINISTRATION));
        return example;
    }

    @Transactional
    public PessoaMongo save(String name) {
        PessoaMongo example = PessoaMongo.builder().name(name).build();
        repository.save(example);
        aclService.adiciona(example);
        return example;
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

}
