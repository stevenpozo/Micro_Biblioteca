package com.microservice_loan.microservice_loan.client;

import com.microservice_loan.microservice_loan.Models.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", url = "http://localhost:8081/user")
public interface UserFeignClient {
    @GetMapping("/{id}")
    ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id);
}

