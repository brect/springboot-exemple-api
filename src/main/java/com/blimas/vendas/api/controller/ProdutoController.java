package com.blimas.vendas.api.controller;

import com.blimas.vendas.domain.entity.Cliente;
import com.blimas.vendas.domain.entity.Produto;
import com.blimas.vendas.domain.repository.ProdutoRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Supplier;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoRepository repository;
    private final static String PRODUCT_NOT_FOUND = "Produto não encontrado";

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public Produto getById(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(
                        responseNotFound(PRODUCT_NOT_FOUND)
                );
    }

    @GetMapping
    public List<Produto> getByFilter(Produto request) {
        ExampleMatcher exampleMatcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(CONTAINING);

        Example example = Example.of(request, exampleMatcher);

        return repository.findAll(example);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto saveProduct(@RequestBody @Valid Produto request) {
        return repository.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduto(@PathVariable Integer id, @RequestBody @Valid Produto request) {
        repository.findById(id)
                .map(produto -> {
                    request.setId(produto.getId());
                    repository.save(request);
                    return Void.TYPE;
                }).orElseThrow(responseNotFound(PRODUCT_NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Integer id) {
        repository.findById(id)
                .map(produto -> {
                    repository.delete(produto);
                    return Void.TYPE;
                })
                .orElseThrow(responseNotFound(PRODUCT_NOT_FOUND));
    }

    private Supplier<ResponseStatusException> responseNotFound(String s) {
        return () -> new ResponseStatusException(HttpStatus.NOT_FOUND, s);
    }


}
