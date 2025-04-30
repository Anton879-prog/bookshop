package com.example.bookshop2.controller;

import com.example.bookshop2.dto.CreatePublisherDto;
import com.example.bookshop2.dto.PublisherDto;
import com.example.bookshop2.service.PublisherService;
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
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    // Получение списка всех издателей
    @GetMapping
    public List<PublisherDto> getAllPublishers() {
        return publisherService.findAll();
    }

    // Получение издателя по ID
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDto> getPublisherById(@PathVariable Long id) {
        PublisherDto publisher = publisherService.findById(id);
        return ResponseEntity.ok(publisher);
    }

    // Создание нового издателя
    @PostMapping
    public ResponseEntity<PublisherDto> createPublisher(
            @RequestBody @Valid CreatePublisherDto dto) {
        PublisherDto created = publisherService.create(dto);
        return ResponseEntity.ok(created);
    }

    // Обновление существующего издателя
    @PutMapping("/{id}")
    public ResponseEntity<PublisherDto> updatePublisher(
            @PathVariable Long id, @RequestBody @Valid CreatePublisherDto dto) {
        PublisherDto updated = publisherService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Удаление издателя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}