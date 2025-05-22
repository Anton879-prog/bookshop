package com.example.bookshop2.service;

import com.example.bookshop2.dto.AuthorDto;
import com.example.bookshop2.dto.CreateAuthorDto;
import com.example.bookshop2.exception.AuthorNotFoundException;
import com.example.bookshop2.mapper.AuthorMapper;
import com.example.bookshop2.model.Author;
import com.example.bookshop2.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final CacheManager cacheManager;

    public AuthorService(AuthorRepository authorRepository, CacheManager cacheManager) {
        this.authorRepository = authorRepository;
        this.cacheManager = cacheManager;
    }

    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(AuthorMapper::toDto)
                .toList();
    }

    public AuthorDto findById(Long id) {
        String cacheKey = "author_" + id;
        AuthorDto cachedAuthor = cacheManager.getFromCache(cacheKey, AuthorDto.class);
        if (cachedAuthor != null) {
            return cachedAuthor;
        }

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        AuthorDto authorDto = AuthorMapper.toDto(author);
        cacheManager.saveToCache(cacheKey, authorDto);
        return authorDto;
    }

    @Transactional
    public AuthorDto create(CreateAuthorDto dto) {
        if (dto == null || dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        Author author = new Author(null, dto.getName(), null);
        Author savedAuthor = authorRepository.save(author);


        cacheManager.clearAuthorCache(savedAuthor.getId());

        return AuthorMapper.toDto(savedAuthor);
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException(id);
        }
        authorRepository.deleteById(id);


        cacheManager.clearAuthorCache(id);
    }

    @Transactional
    public AuthorDto update(Long id, String name) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        author.setName(name);
        Author savedAuthor = authorRepository.save(author);


        cacheManager.clearAuthorCache(id);

        return AuthorMapper.toDto(savedAuthor);
    }
}