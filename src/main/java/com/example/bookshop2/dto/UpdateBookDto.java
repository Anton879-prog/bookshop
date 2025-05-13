package com.example.bookshop2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class UpdateBookDto {
    private String name;
    private String genre;
    private String publisherName;
    private Set<String> authorNames;
}