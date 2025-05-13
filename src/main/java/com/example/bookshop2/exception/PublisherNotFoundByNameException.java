package com.example.bookshop2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PublisherNotFoundByNameException extends RuntimeException {
    public PublisherNotFoundByNameException(String name) {
        super("Publisher with name " + name + " not found");
    }
}