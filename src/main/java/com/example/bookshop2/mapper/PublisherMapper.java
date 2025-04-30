package com.example.bookshop2.mapper;

import com.example.bookshop2.dto.CreatePublisherDto;
import com.example.bookshop2.dto.PublisherDto;
import com.example.bookshop2.model.Publisher;

public class PublisherMapper {

    public static PublisherDto toDto(Publisher publisher) {
        PublisherDto dto = new PublisherDto();
        dto.setId(publisher.getId());
        dto.setName(publisher.getName());
        return dto;
    }

    public static Publisher fromCreateDto(CreatePublisherDto dto) {
        Publisher publisher = new Publisher();
        publisher.setName(dto.getName());
        return publisher;
    }
}
