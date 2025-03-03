package com.irojas.oauth.oauthserver.Client;

import com.irojas.oauth.oauthserver.Modals.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", url = "http://localhost:8081")
public interface UserClient {

    // Define un endpoint en el microservicio de user que devuelva la información del usuario
    @GetMapping("/user/details")
    UserDTO getUserByCode(@RequestParam("code") String code);
}
