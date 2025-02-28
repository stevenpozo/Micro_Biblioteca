package com.microservice_books.microservice_books.Controller;

import com.microservice_books.microservice_books.Entities.Book;
import com.microservice_books.microservice_books.Service.BookService;
import com.microservice_books.microservice_books.Utils.Exceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/book")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;

    // GET ALL BOOKS
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // GET BOOK BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // GET SOME DATA OF BOOKS
    @GetMapping("/some-data")
    public ResponseEntity<List<Map<String, Object>>> getBookSomeData() {
        List<Map<String, Object>> booksData = bookService.getBookSomeData();
        if (booksData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(booksData, HttpStatus.OK);
    }

    // GET ACTIVE DATA OF BOOKS
    @GetMapping("/active")
    public ResponseEntity<List<Map<String, Object>>> getBookActiveData() {
        List<Map<String, Object>> booksData = bookService.getBookActiveData();
        if (booksData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(booksData, HttpStatus.OK);
    }

    // GET SOME DATA OF BOOKS TITLE, AUTHOR AND CODE
    @GetMapping("/tac")
    public ResponseEntity<List<Map<String, Object>>> getBookTac() {
        List<Map<String, Object>> booksData = bookService.getBookTac();
        if (booksData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(booksData, HttpStatus.OK);
    }

    // CREATE NEW BOOK
    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = Exceptions.getExceptionsErrors(result);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            Book newBook = bookService.saveBook(book);
            return new ResponseEntity<>(newBook, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // UPDATE BOOK
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @Valid @RequestBody Book updatedBook, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = Exceptions.getExceptionsErrors(result);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            Book book = bookService.updateBook(updatedBook, id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // DISABLE BOOK
    @PutMapping("/disable/{id}")
    public ResponseEntity<?> disableBook(@PathVariable Integer id) {
        try {
            Book book = bookService.disableBook(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ENABLE BOOK
    @PutMapping("/enable/{id}")
    public ResponseEntity<?> enableBook(@PathVariable Integer id) {
        try {
            Book book = bookService.enableBook(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET TOTAL AMOUNT OF BOOKS
    @GetMapping("/total-amount")
    public ResponseEntity<BigDecimal> getTotalAmount() {
        try {
            BigDecimal totalAmount = bookService.totalAmountBook();
            return new ResponseEntity<>(totalAmount, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // VERIFY IF BOOK STATUS IS ACTIVE
    @GetMapping("/verify-status/{id}")
    public ResponseEntity<Boolean> verifyBookStatus(@PathVariable Integer id) {
        try {
            boolean isActive = bookService.verifyBookStatus(id);
            return new ResponseEntity<>(isActive, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // En caso de error o libro no encontrado
        }
    }

}
