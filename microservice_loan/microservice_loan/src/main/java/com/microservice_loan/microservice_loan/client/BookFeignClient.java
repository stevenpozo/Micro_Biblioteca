package com.microservice_loan.microservice_loan.client;

import com.microservice_loan.microservice_loan.Models.BookDTO;
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
    ResponseEntity<BookDTO> getBook(@PathVariable Long id);

    //GET LIST BOOKS
    @GetMapping
    List<BookDTO> getAllBooks();

    //UPDATE STATUS BOOK TO DISABLE
    @PutMapping("/disable/{id}")
    BookDTO disableBook(@PathVariable("id") Integer id);

    //UPDATE STATUS BOOK TO ENABLE
    @PutMapping("/enable/{id}")
    BookDTO enableBook(@PathVariable("id") Integer id);

    //GET SOME DATA AS TITLE, AUTHOR AND CODE BOOK
    @GetMapping("/tac")
    List<Map<String, Object>> getBookTac();

    //GET STATUS BOOK ACTUAL
    @GetMapping("/verify-status/{id}")
    ResponseEntity<Boolean> verifyBookStatus(@PathVariable("id") Integer id);


}
