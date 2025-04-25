package com.example.bookshop2.controller;

import com.example.bookshop2.model.Author;
import com.example.bookshop2.repository.AuthorRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public List<String> getAllAuthorNames() {
        return authorRepository.findAll().stream()
                .map(Author::getName)
                .collect(Collectors.toList());
    }
}

