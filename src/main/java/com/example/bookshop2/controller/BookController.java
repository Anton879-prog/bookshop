package com.example.bookshop2.controller;

import com.example.bookshop2.dto.BookDto;
import com.example.bookshop2.dto.CreateBookDto;
import com.example.bookshop2.dto.UpdateBookDto;
import com.example.bookshop2.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Метод для санитизации строк перед логированием
    private String sanitize(String input) {
        if (input == null) {
            return null;
        }
        // Удаляем \n, \r, \t и ограничиваем до печатных ASCII
        return input.replaceAll("[\\r\\n\\t]", "_")
                .replaceAll("[^\\x20-\\x7E]", "_");
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        LOGGER.info("Fetching all books"); // NOSONAR
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        LOGGER.info("Fetching book with ID: {}", id); // NOSONAR
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/publisher")
    public ResponseEntity<List<BookDto>> getBooksByPublisher(@RequestParam String name) {
        String sanitizedName = sanitize(name);
        LOGGER.info("Querying books for publisher: {}", sanitizedName); // NOSONAR
        return ResponseEntity.ok(bookService.findByPublisherName(name));
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid CreateBookDto dto) {
        String sanitizedTitle = sanitize(dto.getName());
        LOGGER.info("Creating book with title: {}", sanitizedTitle); // NOSONAR
        return ResponseEntity.ok(bookService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,
                                              @RequestBody UpdateBookDto dto) {
        String sanitizedTitle = sanitize(dto.getName());
        LOGGER.info("Updating book with ID: {}, title: {}", id, sanitizedTitle); // NOSONAR
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        LOGGER.info("Deleting book with ID: {}", id); // NOSONAR
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}