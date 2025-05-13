package com.example.bookshop2.mapper;

import com.example.bookshop2.dto.PublisherDto;
import com.example.bookshop2.model.Publisher;

public class PublisherMapper {

    private PublisherMapper() {}

    public static PublisherDto toDto(Publisher publisher) {
        PublisherDto dto = new PublisherDto();
        dto.setId(publisher.getId());
        dto.setName(publisher.getName());
        return dto;
    }

}
