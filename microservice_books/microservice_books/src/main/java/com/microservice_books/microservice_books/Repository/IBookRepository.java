package com.microservice_books.microservice_books.Repository;

import com.microservice_books.microservice_books.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByCode(String code);
}
