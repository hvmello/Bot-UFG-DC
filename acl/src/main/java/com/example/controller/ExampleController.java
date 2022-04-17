package com.example.controller;

import com.example.model.Example;
import com.example.repository.ExampleRepository;
import com.example.service.ExampleService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ExampleController {

	@Autowired
	ExampleRepository exampleRepository;

	@Autowired
	ExampleService service;
	
	@GetMapping
	public ResponseEntity<String> get() {
		return  ResponseEntity.ok().body("Não Autenticado");
	}
	
	@GetMapping("/security")
	public ResponseEntity<String> getSecurity() {
		return  ResponseEntity.ok().body("Autenticado");
	}
	
	
	@PreAuthorize ("hasRole('user')")
	@GetMapping("/security2")
	public ResponseEntity<String> getSecurity2() { 
		return  ResponseEntity.ok().body("Autenticado 2");
	}
	
	@PreAuthorize ("hasRole('user')")
	@PostMapping
	@Transactional
	public ResponseEntity<Example> saveExample() { 
		return  ResponseEntity.ok().body(service.save("Example"));
	}
	
	@GetMapping("/example/{id}")
	public ResponseEntity<Example> getExample(@PathVariable Long id) throws NotFoundException { 
		return  ResponseEntity.ok().body(service.find(id));
	}

	@GetMapping("/post-filter/{id}")
	@PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, admin)")
	public List<Example> getExamplePosFilter(@PathVariable Long id) throws NotFoundException {
		return  exampleRepository.findAll();
	}

	@GetMapping("/post-filter2/{id}")
	@PreAuthorize("hasPermission(#id, 'com.example.model.Example', read) or " +
			"hasPermission(#id, 'com.example.model.Example', admin)")
	public ResponseEntity<Example> getExamplePosFilter2(@PathVariable Long id) throws NotFoundException {
		return  ResponseEntity.ok().body(exampleRepository.findById(id).get());
	}


	@PreAuthorize("hasPermission(#id, 'com.example.model.Example', delete)")
	@DeleteMapping("/{id}")
	public ResponseEntity<Example> delete(@PathVariable Long id) throws NotFoundException {
		return  ResponseEntity.ok().body(exampleRepository.findById(id).get());
	}


//	@PostAuthorize("returnObject.name == authentication.principal.userName")
	@PostAuthorize("returnObject.name == authentication.principal.context.token.preferredUsername")
	@GetMapping("/by-name/{name}")
	public Example loadUserDetail(@PathVariable String name) {
		Example example = exampleRepository.findByName(name);
		return  example;
	}

}
