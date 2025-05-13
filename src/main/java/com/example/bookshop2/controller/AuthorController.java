package com.example.bookshop2.controller;

import com.example.bookshop2.dto.AuthorDto;
import com.example.bookshop2.dto.CreateAuthorDto;
import com.example.bookshop2.service.AuthorService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @PostMapping
    public AuthorDto createAuthor(@RequestBody CreateAuthorDto dto) {
        return authorService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.delete(id);
    }

    @PutMapping("/{id}")
    public AuthorDto updateAuthor(@PathVariable Long id, @RequestBody CreateAuthorDto dto) {
        return authorService.update(id, dto.getName());
    }
}