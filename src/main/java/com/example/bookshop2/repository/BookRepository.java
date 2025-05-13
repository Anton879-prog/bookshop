package com.example.bookshop2.repository;

import com.example.bookshop2.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.publisher.name = :publisherName")
    List<Book> findByPublisherNameJpql(@Param("publisherName") String publisherName);
    /*
    @Query(value = "SELECT * FROM books b JOIN publishers p ON b.publisher_id = p.id "
            + "WHERE p.name = :publisherName", nativeQuery = true)
    List<Book> findByPublisherNameNative(@Param("publisherName") String publisherName);

     */
}