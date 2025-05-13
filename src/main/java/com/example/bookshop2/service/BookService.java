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
        String cacheKey = "book_" + id;
        BookDto cachedBook = cacheManager.getFromCache(cacheKey, BookDto.class);
        if (cachedBook != null) {
            return cachedBook;
        }

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        BookDto bookDto = BookMapper.toDto(book);
        cacheManager.saveToCache(cacheKey, bookDto);
        return bookDto;
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
        Book savedBook = bookRepository.save(book);

        // Очистка кэша для издателя, книги и авторов
        cacheManager.clearPublisherCache(dto.getPublisherName());
        cacheManager.clearBookCache(savedBook.getId());
        authors.forEach(author -> cacheManager.clearAuthorCache(author.getId()));

        return BookMapper.toDto(savedBook);
    }

    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        String publisherName = book.getPublisher() != null ? book.getPublisher().getName() : null;


        bookRepository.deleteById(id);

        Set<Long> authorIds = book.getAuthors().stream()
                .map(Author::getId)
                .collect(Collectors.toSet());

        // Очистка кэша для издателя, книги и авторов
        if (publisherName != null) {
            cacheManager.clearPublisherCache(publisherName);
        }
        cacheManager.clearBookCache(id);
        authorIds.forEach(cacheManager::clearAuthorCache);
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

        String oldPublisherName = book.getPublisher() != null
                ? book.getPublisher().getName() : null;

        Set<Author> authors = null;
        if (dto.getAuthorNames() != null) {
            authors = dto.getAuthorNames().stream()
                    .map(name -> authorRepository.findByName(name)
                            .orElseGet(() -> authorRepository.save(new Author(null, name, null))))
                    .collect(Collectors.toSet());
        }

        BookMapper.updateFromDto(book, dto, authors, publisher);




        // Очистка кэша для старого и нового издателя, книги и авторов
        if (oldPublisherName != null) {
            cacheManager.clearPublisherCache(oldPublisherName);
        }
        if (dto.getPublisherName() != null) {
            cacheManager.clearPublisherCache(dto.getPublisherName());
        }
        cacheManager.clearBookCache(id);

        Book savedBook = bookRepository.save(book);

        Set<Long> oldAuthorIds = book.getAuthors().stream()
                .map(Author::getId)
                .collect(Collectors.toSet());

        oldAuthorIds.forEach(cacheManager::clearAuthorCache);
        if (authors != null) {
            authors.forEach(author -> cacheManager.clearAuthorCache(author.getId()));
        }

        return BookMapper.toDto(savedBook);
    }

    public List<BookDto> findByPublisherName(String publisherName) {
        if (publisherRepository.findByName(publisherName).isEmpty()) {
            throw new PublisherNotFoundByNameException(publisherName);
        }

        String cacheKey = "publishers_" + publisherName;
        @SuppressWarnings("unchecked")
        List<BookDto> cachedBooks = cacheManager.getFromCache(cacheKey, List.class);
        if (cachedBooks != null) {
            return cachedBooks;
        }

        List<BookDto> books = bookRepository.findByPublisherNameNative(publisherName).stream()
                .map(BookMapper::toDto)
                .toList();

        cacheManager.saveToCache(cacheKey, books);
        return books;
    }
}