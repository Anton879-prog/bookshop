package com.example.bookshop2.service;

import com.example.bookshop2.dto.PublisherDto;
import com.example.bookshop2.exception.PublisherNotFoundException;
import com.example.bookshop2.mapper.PublisherMapper;
import com.example.bookshop2.model.Publisher;
import com.example.bookshop2.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final CacheManager cacheManager;
    private final String publisherString = "publisher_";

    public PublisherService(PublisherRepository publisherRepository, CacheManager cacheManager) {
        this.publisherRepository = publisherRepository;
        this.cacheManager = cacheManager;
    }

    public List<PublisherDto> findAll() {
        return publisherRepository.findAll().stream()
                .map(PublisherMapper::toDto)
                .toList();
    }

    public PublisherDto findById(Long id) {
        String cacheKey = publisherString + id;
        PublisherDto cachedPublisher = cacheManager.getFromCache(cacheKey, PublisherDto.class);
        if (cachedPublisher != null) {
            return cachedPublisher;
        }

        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));
        PublisherDto publisherDto = PublisherMapper.toDto(publisher);
        cacheManager.saveToCache(cacheKey, publisherDto);
        return publisherDto;
    }

    @Transactional
    public PublisherDto create(String name) {
        Publisher publisher = new Publisher(null, name, null);
        Publisher savedPublisher = publisherRepository.save(publisher);

        // Очистка кэша для нового издателя
        cacheManager.clearPublisherCache(savedPublisher.getName());
        cacheManager.clearByPrefix(publisherString + savedPublisher.getId());

        return PublisherMapper.toDto(savedPublisher);
    }

    public void delete(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));
        String publisherName = publisher.getName();

        publisherRepository.deleteById(id);

        // Очистка кэша для издателя
        cacheManager.clearPublisherCache(publisherName);
        cacheManager.clearByPrefix(publisherString + id);
    }

    @Transactional
    public PublisherDto update(Long id, String name) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));
        String oldPublisherName = publisher.getName();

        publisher.setName(name);

        // Очистка кэша для старого и нового имени издателя
        cacheManager.clearPublisherCache(oldPublisherName);
        cacheManager.clearPublisherCache(name);
        cacheManager.clearByPrefix(publisherString + id);
        Publisher savedPublisher = publisherRepository.save(publisher);
        return PublisherMapper.toDto(savedPublisher);
    }
}