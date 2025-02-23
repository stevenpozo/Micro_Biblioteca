package com.microservice_loan.microservice_loan.Client;

import com.microservice_loan.microservice_loan.Model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", url = "http://localhost:8081")
public interface ClientUser {

    @GetMapping("/users/{id}")
    User getUserById(@PathVariable("id") Long id);
}
