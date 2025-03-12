package com.example.bookshop2.service;

import com.example.bookshop2.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final List<Book> books = new ArrayList<>();

    public BookService() {
        books.add(new Book(1L, "Rivers", "Adventure"));
        books.add(new Book(2L, "Physics", "Science"));
        books.add(new Book(3L, "English", "Education"));
        books.add(new Book(4L, "Cars", "Magazine"));
    }

    public Optional<Book> getBookById(Long id) {
        return books.stream().filter(book -> book.getId() == id).findFirst();
    }

    public List<Book> getBooksByName(String name) {
        return books.stream().filter(book -> book.getName().equals(name))
                .collect(Collectors.toList());
    }
}
