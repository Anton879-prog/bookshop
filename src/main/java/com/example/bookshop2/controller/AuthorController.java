package com.example.bookshop2.controller;

import com.example.bookshop2.dto.AuthorDto;
import com.example.bookshop2.dto.CreateAuthorDto;
import com.example.bookshop2.service.AuthorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
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

    // Получение списка всех авторов
    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        return authorService.findAll();
    }

    // Получение автора по ID
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {
        AuthorDto author = authorService.findById(id);
        return ResponseEntity.ok(author);
    }

    // Создание нового автора
    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody @Valid CreateAuthorDto dto) {
        AuthorDto created = authorService.create(dto);
        return ResponseEntity.ok(created);
    }

    // Обновление существующего автора
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto>
        updateAuthor(@PathVariable Long id, @RequestBody @Valid CreateAuthorDto dto) {
        AuthorDto updated = authorService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Удаление автора
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
