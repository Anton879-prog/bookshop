package com.example.bookshop2.service;

import com.example.bookshop2.dto.BookDto;
import com.example.bookshop2.dto.CreateBookDto;
import com.example.bookshop2.dto.UpdateBookDto;
import com.example.bookshop2.exception.BookNotFoundException;
import com.example.bookshop2.exception.PublisherNotFoundByNameException;
import com.example.bookshop2.mapper.BookMapper;
import com.example.bookshop2.model.Author;
import com.example.bookshop2.model.Book;
import com.example.bookshop2.model.Publisher;
import com.example.bookshop2.repository.AuthorRepository;
import com.example.bookshop2.repository.BookRepository;
import com.example.bookshop2.repository.PublisherRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final CacheManager cacheManager;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       PublisherRepository publisherRepository,
                       CacheManager cacheManager) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.cacheManager = cacheManager;
    }

    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toDto)
                .toList();
    }

    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return BookMapper.toDto(book);
    }

    @Transactional
    public BookDto create(CreateBookDto dto) {
        Publisher publisher = publisherRepository.findByName(dto.getPublisherName())
                .orElseGet(() -> publisherRepository
                        .save(new Publisher(null, dto.getPublisherName(), null)));

        Set<Author> authors = dto.getAuthorNames().stream()
                .map(name -> authorRepository.findByName(name)
                        .orElseGet(() -> authorRepository.save(new Author(null, name, null))))
                .collect(Collectors.toSet());

        Book book = BookMapper.fromCreateDto(dto, authors, publisher);
        return BookMapper.toDto(bookRepository.save(book));
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    @Transactional
    public BookDto update(Long id, UpdateBookDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        Publisher publisher = null;
        if (dto.getPublisherName() != null) {
            publisher = publisherRepository.findByName(dto.getPublisherName())
                    .orElseGet(() -> publisherRepository
                            .save(new Publisher(null, dto.getPublisherName(), null)));
        }

        Set<Author> authors = null;
        if (dto.getAuthorNames() != null) {
            authors = dto.getAuthorNames().stream()
                    .map(name -> authorRepository.findByName(name)
                            .orElseGet(() -> authorRepository.save(new Author(null, name, null))))
                    .collect(Collectors.toSet());
        }

        BookMapper.updateFromDto(book, dto, authors, publisher);
        return BookMapper.toDto(bookRepository.save(book));
    }

    public List<BookDto> findByPublisherName(String publisherName) {
        if (publisherRepository.findByName(publisherName).isEmpty()) {
            throw new PublisherNotFoundByNameException(publisherName);
        }

        List<BookDto> cachedBooks = cacheManager.getBooksByPublisher(publisherName);
        if (cachedBooks != null) {
            return cachedBooks;
        }

        List<BookDto> books = bookRepository.findByPublisherNameJpql(publisherName).stream()
                .map(BookMapper::toDto)
                .toList();

        cacheManager.putBooksByPublisher(publisherName, books);
        return books;
    }
}