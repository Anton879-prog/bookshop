package com.example.bookshop2.mapper;

import com.example.bookshop2.dto.AuthorDto;
import com.example.bookshop2.dto.CreateAuthorDto;
import com.example.bookshop2.model.Author;

public class AuthorMapper {

    private AuthorMapper() {}

    public static AuthorDto toDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        return dto;
    }

    public static Author fromCreateDto(CreateAuthorDto dto) {
        Author author = new Author();
        author.setName(dto.getName());
        return author;
    }
}
