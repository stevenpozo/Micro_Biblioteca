package com.microservice_users.microservice_users.Client;

import com.microservice_users.microservice_users.Models.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "book", url = "http://localhost:8082/book")
public interface BookFeignClient {

    //GET ALL BOOK (MICROSERVICIO BOOK)
    @GetMapping("/{id}")
    ResponseEntity<Book> getBook(@PathVariable Long id);

    //GET LIST BOOKS
    @GetMapping
    List<Book> getAllBooks();

    //UPDATE STATUS BOOK TO DISABLE
    @PutMapping("/disable/{id}")
    Book disableBook(@PathVariable("id") Integer id);

    //UPDATE STATUS BOOK TO ENABLE
    @PutMapping("/enable/{id}")
    Book enableBook(@PathVariable("id") Integer id);

    //GET SOME DATA AS TITLE, AUTHOR AND CODE BOOK
    @GetMapping("/tac")
    List<Map<String, Object>> getBookTac();

    //GET STATUS BOOK ACTUAL
    @GetMapping("/verify-status/{id}")
    ResponseEntity<Boolean> verifyBookStatus(@PathVariable("id") Integer id);


}
