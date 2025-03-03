package com.microservice_users.microservice_users.Service;

import com.microservice_users.microservice_users.Entities.User;
import com.microservice_users.microservice_users.Modals.UserDTO;
import com.microservice_users.microservice_users.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCK_TIME_DURATION = 2; // in minutes

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //LOGIN
    public User login(String code, String password) {
        try {
            Optional<User> userOptional = userRepository.findByCode(code);

            if (!userOptional.isPresent()) {
                throw new RuntimeException("User not found");
            }

            User user = userOptional.get();

            // Verificar si el usuario está bloqueado
            if (user.getLock_time() != null) {
                LocalDateTime lockTime = user.getLock_time().toLocalDateTime();
                long minutesPassed = ChronoUnit.MINUTES.between(lockTime, LocalDateTime.now());

                if (minutesPassed < LOCK_TIME_DURATION) {
                    long minutesLeft = LOCK_TIME_DURATION - minutesPassed;
                    throw new RuntimeException("Account is locked. Try again in " + minutesLeft + " minutes.");
                } else {
                    // Desbloquear la cuenta después de que haya pasado el tiempo
                    unlockUserAccount(user.getId_user());
                }
            }

            // Verificación de la contraseña
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Contraseña correcta, restablecer intentos fallidos y tiempo de bloqueo
                user.setFailed_attempts(0);
                user.setLock_time(null);
                userRepository.save(user);
                return user; // Devolver el usuario como JSON
            } else {
                // Contraseña incorrecta, incrementar intentos fallidos
                int failedAttempts = user.getFailed_attempts() + 1;
                user.setFailed_attempts(failedAttempts);

                if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                    // Bloquear la cuenta
                    user.setStatus(false);
                    user.setLock_time(Timestamp.valueOf(LocalDateTime.now()));
                    userRepository.save(user);
                    throw new RuntimeException("Account locked due to too many failed login attempts. Try again in " + LOCK_TIME_DURATION + " minutes.");
                } else {
                    userRepository.save(user);
                    int attemptsLeft = MAX_FAILED_ATTEMPTS - failedAttempts;
                    throw new RuntimeException("Invalid credentials. You have " + attemptsLeft + " attempt(s) left.");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }




    // GET ALL USERS
    public List<User> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                return new ArrayList<>();
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Error get all users", e);
        }
    }

    // GET SOME DATA USER
    public List<Map<String, Object>> getUsersSomeData() {
        try {
            List<User> users = getAllUsers();
            List<Map<String, Object>> usersWithoutPassword = new ArrayList<>();
            if (users.isEmpty()) {
                return new ArrayList<>();
            }
            for (User user : users) {
                Map<String, Object> userData = new LinkedHashMap<>();
                userData.put("id_user", user.getId_user());
                userData.put("code", user.getCode());
                userData.put("role", user.getRole());
                userData.put("first_name", user.getFirst_name());
                userData.put("last_name", user.getLast_name());
                userData.put("mail", user.getMail());

                usersWithoutPassword.add(userData);
            }
            return usersWithoutPassword;
        } catch (Exception e) {
            throw new RuntimeException("Error get list of users", e);
        }
    }

    // SAVE A NEW ADMINISTRATOR USER
    public User saveUser(User user) {
        Optional<User> existingUser = userRepository.findByCode(user.getCode());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with code id " + user.getCode() + " is already registered in the system");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(true);

        return userRepository.save(user);
    }

    // REGISTER A NEW NATURAL USER
    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByCode(user.getCode());
        if (existingUser.isPresent()) {
            throw new RuntimeException("The user with ID number " + user.getCode() + " is already registered in the system");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setStatus(true);

        return userRepository.save(user);
    }

    // GET USER BY ID
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // UPDATE USER BY ADMIN
    public User updateUserByAdmin(User request, Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!user.getCode().equals(request.getCode())) {
                Optional<User> existingUser = userRepository.findByCode(request.getCode());
                if (existingUser.isPresent()) {
                    throw new RuntimeException("The code" + request.getCode() + " is already registered in the system");
                }
            }

            user.setFirst_name(request.getFirst_name());
            user.setLast_name(request.getLast_name());
            user.setMail(request.getMail());
            user.setRole(request.getRole());
            user.setCode(request.getCode());

            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // UPDATE USER BY USER
    public User updateUserByUser(User request, Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!"USER".equalsIgnoreCase(user.getRole())) {
                throw new RuntimeException("Only natural user can edit");
            }

            user.setFirst_name(request.getFirst_name());
            user.setLast_name(request.getLast_name());
            user.setMail(request.getMail());

            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found in the database");
        }
    }


    // UNLOCK USER ACCOUNT
    public User unlockUserAccount(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(true);
            user.setFailed_attempts(0);
            user.setLock_time(null);

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found in the database");
        }
    }

    // DISABLE A USER
    public User disableUser(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(false);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found in the database");
        }
    }

    //GET USER DETAILS
    public UserDTO getUserDetailsByCode(String code) {
        Optional<User> userOptional = userRepository.findByCode(code);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Usuario no encontrado con code: " + code);
        }
        User user = userOptional.get();
        UserDTO dto = new UserDTO();
        // Aquí puedes decidir qué campos devolver; en este ejemplo, se mantienen los mismos:
        dto.setCode(user.getCode());
        dto.setPassword(user.getPassword()); // La contraseña encriptada
        dto.setRole(user.getRole());
        return dto;
    }

}
