package com.example.bookshop2.dto;

import java.util.Set;
import lombok.Data;



@Data
public class CreateBookDto {
    private String name;
    private String genre;
    private String publisherName;
    private Set<String> authorNames;
}
