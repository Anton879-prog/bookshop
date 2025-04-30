package com.example.bookshop2.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAuthorDto {
    @NotBlank
    private String name;
}