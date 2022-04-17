package com.example.service;

import com.example.model.Example;
import com.example.repository.ExampleRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class ExampleService {

	@Autowired
	private ExampleRepository repository;

	@Autowired
	private AclService aclService;

	public Example find(Long id) throws NotFoundException {
		Example example = repository.findById(id).get();
		aclService.hasPermission(example, Arrays.asList(BasePermission.READ,BasePermission.ADMINISTRATION));
		return example;
	}

	@Transactional
	public Example save(String name) {
		Example example = Example.builder().name(name).build();
		repository.save(example);
		aclService.adiciona(example);
		return example;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

}
