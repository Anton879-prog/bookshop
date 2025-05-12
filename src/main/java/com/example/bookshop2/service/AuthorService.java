package com.example.bookshop2.service;

import com.example.bookshop2.dto.AuthorDto;
import com.example.bookshop2.dto.CreateAuthorDto;
import com.example.bookshop2.exception.AuthorNotFoundException;
import com.example.bookshop2.mapper.AuthorMapper;
import com.example.bookshop2.model.Author;
import com.example.bookshop2.model.Book;
import com.example.bookshop2.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(AuthorMapper::toDto)
                .toList();
    }

    public AuthorDto findById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        return AuthorMapper.toDto(author);
    }

    @Transactional
    public AuthorDto create(CreateAuthorDto dto) {
        Author author = AuthorMapper.fromCreateDto(dto);
        return AuthorMapper.toDto(authorRepository.save(author));
    }

    @Transactional
    public AuthorDto update(Long id, CreateAuthorDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        author.setName(dto.getName());
        return AuthorMapper.toDto(authorRepository.save(author));
    }

    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));


        for (Book book : author.getBooks()) {
            book.getAuthors().remove(author);
        }


        authorRepository.delete(author);
    }
}