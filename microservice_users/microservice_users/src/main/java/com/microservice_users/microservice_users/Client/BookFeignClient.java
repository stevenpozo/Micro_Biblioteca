package com.microservice_users.microservice_users.Client;

import com.microservice_users.microservice_users.Entities.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "book", url = "http://localhost:8082/book")
public interface BookFeignClient {

    @GetMapping("/{id}")
    ResponseEntity<Book> getBook(@PathVariable Long id);

    @GetMapping
    List<Book> getAllBooks();
}
