package com.microservice_users.microservice_users.Repository;

import com.microservice_users.microservice_users.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Clase que permite hacer querys a una base de datos

public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByCode(String code);

}