package com.example.bookshop2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Bookshop2Application {
    public static void main(String[] args) {
        SpringApplication.run(Bookshop2Application.class, args);
    }
}
