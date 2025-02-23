package com.microservice_loan.microservice_loan.Client;

import com.microservice_loan.microservice_loan.Model.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book", url = "http://localhost:8082")
public interface ClientBook {

    @GetMapping("/books/{id}")
    Book getBookById(@PathVariable("id") Long id);
}

