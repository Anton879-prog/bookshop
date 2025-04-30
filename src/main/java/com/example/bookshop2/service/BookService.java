package com.example.bookshop2.service;

import com.example.bookshop2.dto.BookDto;
import com.example.bookshop2.dto.CreateBookDto;
import com.example.bookshop2.exception.BookNotFoundException;
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

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
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
        bookRepository.deleteById(id);
    }

    @Transactional
    public BookDto update(Long id, CreateBookDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        Publisher publisher = publisherRepository.findByName(dto.getPublisherName())
                .orElseGet(() -> publisherRepository
                        .save(new Publisher(null, dto.getPublisherName(), null)));

        Set<Author> authors = dto.getAuthorNames().stream()
                .map(name -> authorRepository.findByName(name)
                        .orElseGet(() -> authorRepository.save(new Author(null, name, null))))
                .collect(Collectors.toSet());

        book.setName(dto.getName());
        book.setGenre(dto.getGenre());
        book.setPublisher(publisher);
        book.setAuthors(authors);

        return BookMapper.toDto(bookRepository.save(book));
    }

}
