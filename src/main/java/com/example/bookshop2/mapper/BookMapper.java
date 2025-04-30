package com.example.bookshop2.mapper;

import com.example.bookshop2.dto.BookDto;
import com.example.bookshop2.dto.CreateBookDto;
import com.example.bookshop2.model.Author;
import com.example.bookshop2.model.Book;
import com.example.bookshop2.model.Publisher;
import java.util.Set;
import java.util.stream.Collectors;

public class BookMapper {

    private BookMapper() {}

    public static BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setName(book.getName());
        dto.setGenre(book.getGenre());

        Publisher publisher = book.getPublisher();
        dto.setPublisherName(publisher != null ? publisher.getName() : "Deleted");

        Set<Author> authors = book.getAuthors();
        if (authors == null || authors.isEmpty()) {
            dto.setAuthors(Set.of("Deleted"));
        } else {
            dto.setAuthors(
                    authors.stream()
                            .map(Author::getName)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    public static Book fromCreateDto(CreateBookDto dto, Set<Author> authors, Publisher publisher) {
        Book book = new Book();
        book.setName(dto.getName());
        book.setGenre(dto.getGenre());
        book.setAuthors(authors);
        book.setPublisher(publisher);
        return book;
    }
}
