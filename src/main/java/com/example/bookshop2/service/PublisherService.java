package com.example.bookshop2.service;

import com.example.bookshop2.dto.CreatePublisherDto;
import com.example.bookshop2.dto.PublisherDto;
import com.example.bookshop2.exception.PublisherNotFoundException;
import com.example.bookshop2.mapper.PublisherMapper;
import com.example.bookshop2.model.Book;
import com.example.bookshop2.model.Publisher;
import com.example.bookshop2.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherDto> findAll() {
        return publisherRepository.findAll().stream()
                .map(PublisherMapper::toDto)
                .toList();
    }

    public PublisherDto findById(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        return PublisherMapper.toDto(publisher);
    }

    @Transactional
    public PublisherDto create(CreatePublisherDto dto) {
        Publisher publisher = PublisherMapper.fromCreateDto(dto);
        return PublisherMapper.toDto(publisherRepository.save(publisher));
    }

    @Transactional
    public PublisherDto update(Long id, CreatePublisherDto dto) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        publisher.setName(dto.getName());
        return PublisherMapper.toDto(publisherRepository.save(publisher));
    }

    @Transactional
    public void delete(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));

        // Разрываем связь с книгами
        for (Book book : publisher.getBooks()) {
            book.setPublisher(null);
        }

        publisherRepository.delete(publisher);
    }

}