package com.example.bookshop2.service;

import com.example.bookshop2.dto.BookDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CacheManager {
    private final Map<String, List<BookDto>> booksByPublisherCache = new HashMap<>();

    public List<BookDto> getBooksByPublisher(String publisherName) {
        return booksByPublisherCache.get(publisherName);
    }

    public void putBooksByPublisher(String publisherName, List<BookDto> books) {
        booksByPublisherCache.put(publisherName, books);
    }
}
