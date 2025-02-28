package com.microservice_users.microservice_users.Controller;

import com.microservice_users.microservice_users.Entities.User;
import com.microservice_users.microservice_users.Service.UserService;
import com.microservice_users.microservice_users.Utils.Exceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // LOGIN METHOD
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String code = loginRequest.get("code");
        String password = loginRequest.get("password");

        try {
            User user = userService.login(code, password);
            return ResponseEntity.ok(user);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }




    // GET ALL USERS
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
            List<User> users = userService.getAllUsers();
            Map<String, Object> response = new HashMap<>();
            
            if (!users.isEmpty()) {
                response.put("users", users);
                return ResponseEntity.ok(response);
            } else {
                response.put("users", Collections.emptyList());
                response.put("message", "No users found, but access granted");
                return ResponseEntity.ok(response);
            }

    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // CREATE A NEW ADMINISTRATOR USER
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = Exceptions.getExceptionsErrors(result);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            User newUser = userService.saveUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // REGISTER A NEW NATURAL USER
    @PostMapping("/register-user")
    public ResponseEntity<?> registerNaturalUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = Exceptions.getExceptionsErrors(result);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            User newUser = userService.registerUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // UPDATE USER BY ADMIN
    @PutMapping("/update-admin/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @Valid @RequestBody User updatedUser, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = Exceptions.getExceptionsErrors(result);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.updateUserByAdmin(updatedUser, id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/enable-user/{id}")
    public ResponseEntity<?> unlockUserAccount(@PathVariable Integer id) {
        try {
            User user = userService.unlockUserAccount(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // UPDATE USER BY USER
    @PutMapping("/update-user/{id}")
    public ResponseEntity<?> updateUserByUser(@PathVariable Integer id, @Valid @RequestBody User updatedUser, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = Exceptions.getExceptionsErrors(result);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.updateUserByUser(updatedUser, id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // DISABLE USER
    @PutMapping("/disable-user/{id}")
    public ResponseEntity<User> disableUser(@PathVariable Integer id) {
        try {
            User user = userService.disableUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // GET SOME DATA USER
    @GetMapping("/some-data")
    public ResponseEntity<List<Map<String, Object>>> getUsersSomeData() {
        List<Map<String, Object>> usersData = userService.getUsersSomeData();
        if (usersData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(usersData, HttpStatus.OK);
    }
}
