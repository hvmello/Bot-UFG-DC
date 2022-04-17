package com.example.controller;


import com.example.model.PessoaMongo;
import com.example.repository.PessoaMongoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/pessoas")
public class PessoaResource {

    @Autowired
    PessoaMongoRepository repository;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String post(@RequestBody PessoaMongo pessoa, HttpServletResponse response){
        pessoa.setId(null);
        repository.save(pessoa);

        response.addHeader(HttpHeaders.LOCATION,
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(pessoa.getId())
                        .toUri().toString());

        return pessoa.getId();
    }


    @GetMapping(path = "/{id}")
    public PessoaMongo getById(@PathVariable("id") String id){
        return repository.findById(id).get();
    }

    @GetMapping
    public List<PessoaMongo> getAll(){
        return repository.findAll();
    }

    @GetMapping("/filter")
    public List<PessoaMongo> getPessoa(
            @RequestParam("_start") Integer _start, @RequestParam("_end") Integer _end,
            @RequestParam("_sort") String _sort, @RequestParam("_order") String _order,  HttpServletResponse response
    ) {
        var result = repository.findAll();
        response.addHeader("X-Total-Count", String.valueOf(result.size()));
        return result;
    }


    @PutMapping(path = "/{id}")
    public void put(@PathVariable String id, @RequestBody PessoaMongo pessoa, HttpServletResponse response) {
        var persistent = getById(id);
        BeanUtils.copyProperties(pessoa, persistent, "id");
        repository.save(persistent);

        response.addHeader(HttpHeaders.LOCATION,
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(pessoa.getId())
                        .toUri().toString());
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") String id) {
        getById(id);
        repository.deleteById(id);
    }

    @Transactional
    @DeleteMapping(path = "/all")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAll() {
        repository.deleteAll();
    }

}

