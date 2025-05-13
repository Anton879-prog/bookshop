package com.example.bookshop2.controller;

import com.example.bookshop2.dto.CreatePublisherDto;
import com.example.bookshop2.dto.PublisherDto;
import com.example.bookshop2.service.PublisherService;
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
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<PublisherDto> getAllPublishers() {
        return publisherService.findAll();
    }

    @GetMapping("/{id}")
    public PublisherDto getPublisherById(@PathVariable Long id) {
        return publisherService.findById(id);
    }

    @PostMapping
    public PublisherDto createPublisher(@RequestBody CreatePublisherDto dto) {
        return publisherService.create(dto.getName());
    }

    @PutMapping("/{id}")
    public PublisherDto updatePublisher(@PathVariable Long id,
                                        @RequestBody CreatePublisherDto dto) {
        return publisherService.update(id, dto.getName());
    }

    @DeleteMapping("/{id}")
    public void deletePublisher(@PathVariable Long id) {
        publisherService.delete(id);
    }
}