package com.microservice_books.microservice_books.Service;


import com.microservice_books.microservice_books.Entities.Book;
import com.microservice_books.microservice_books.Repository.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BookService {

    @Autowired
    IBookRepository bookRepository;


    //GET ALL BOOKS
    public List<Book> getAllBooks(){
        try {
            List<Book> books = bookRepository.findAll();
            if (books.isEmpty()) {
                return new ArrayList<>();
            }
            return books;
        } catch (Exception e) {
            throw new RuntimeException("Error get all books", e);
        }
    }

    //GET B0OK BY ID
    public Optional<Book> getBookById(Integer id){
        return bookRepository.findById(id);
    }

    //GET SOME BOOK DATA
    public List<Map<String, Object>> getBookSomeData(){
        try{
            List<Book> books = getAllBooks();
            List<Map<String, Object>> bookSomeData = new ArrayList<>();

            if (books.isEmpty()) {
                return new ArrayList<>();
            }

            for(Book book:books){
                Map<String, Object> bookData = new LinkedHashMap<>();
                bookData.put("code", book.getCode());
                bookData.put("title", book.getTitle());
                bookData.put("author", book.getAuthor());
                bookData.put("description", book.getDescription());
                bookData.put("price",book.getPrice());
                bookData.put("status", book.getStatus());

                bookSomeData.add(bookData);
            }

            return bookSomeData;
        }catch (Exception e){
            throw new RuntimeException("Error get books", e);
        }
    }

    //GET ACTIVE BOOKS
    public List<Map<String, Object>> getBookActiveData(){
        try{
            List<Book> books = getAllBooks();
            List<Map<String, Object>> bookSomeData = new ArrayList<>();

            if (books.isEmpty()) {
                return new ArrayList<>();
            }

            for(Book book:books){
                if (book.getStatus()) {
                    Map<String, Object> bookData = new LinkedHashMap<>();
                    bookData.put("code", book.getCode());
                    bookData.put("title", book.getTitle());
                    bookData.put("author", book.getAuthor());
                    bookData.put("description", book.getDescription());
                    bookData.put("price", book.getPrice());
                    bookData.put("status", book.getStatus());

                    bookSomeData.add(bookData);
                }
            }
            return bookSomeData;
        }catch (Exception e){
            throw new RuntimeException("Error get books", e);
        }
    }

    //GET TITLE, AUTHOR AND CODE
    public List<Map<String, Object>> getBookTac(){
        try{
            List<Book> books = getAllBooks();
            List<Map<String, Object>> bookSomeData = new ArrayList<>();

            if (books.isEmpty()) {
                return new ArrayList<>();
            }

            for(Book book:books){
                    Map<String, Object> bookData = new LinkedHashMap<>();
                    bookData.put("code", book.getCode());
                    bookData.put("title", book.getTitle());
                    bookData.put("author", book.getAuthor());
                    bookSomeData.add(bookData);
            }
            return bookSomeData;
        }catch (Exception e){
            throw new RuntimeException("Error get books", e);
        }
    }

    //SAVE BOOK
    public Book saveBook(Book book) {
        Optional<Book> existingBook = bookRepository.findByCode(String.valueOf(book.getCode()));
        if (existingBook.isPresent()) {
            throw new RuntimeException("The book with code " + book.getCode() + " is already registered in the system");
        }

        book.setStatus(true);
        return bookRepository.save(book);
    }

    //UPDATE BOOK
    public Book updateBook(Book request, Integer id){
        List<String> errors = new ArrayList<>();
        Optional<Book> existingBook = bookRepository.findById(id);
        if(existingBook.isPresent()){
            Book book = existingBook.get();
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setLanguage(request.getLanguage());
            book.setCode(request.getCode());
            book.setDescription(request.getDescription());
            book.setPhysical_state(request.getPhysical_state());
            book.setPrice(request.getPrice());

            return bookRepository.save(book);
        }else{
            throw new RuntimeException("This book not found");
        }
    }


    //DISABLE BOOK
    public Book disableBook(Integer id) {
        Optional<Book> userOptional = bookRepository.findById(id);
        if (userOptional.isPresent()) {
            Book book = userOptional.get();
            book.setStatus(false);
            return bookRepository.save(book);
        } else {
            throw new RuntimeException("Book not found in the database");
        }
    }

    //ENABLE BOOK
    public Book enableBook(Integer id) {
        Optional<Book> userOptional = bookRepository.findById(id);
        if (userOptional.isPresent()) {
            Book book = userOptional.get();
            book.setStatus(true);
            return bookRepository.save(book);
        } else {
            throw new RuntimeException("Book not found in the database");
        }
    }

    //GET TOTAL AMOUNT
    public BigDecimal totalAmountBook() {
        try {
            List<Book> books = bookRepository.findAll();

            if (books.isEmpty()) {
                return BigDecimal.ZERO;
            }

            BigDecimal totalAmount = books.stream()
                    .map(Book::getPrice)
                    .filter(price -> price != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return totalAmount;
        } catch (Exception e) {
            throw new RuntimeException("Error get total amount", e);
        }
    }

    // VERIFY IF BOOK STATUS IS ACTIVE
    public boolean verifyBookStatus(Integer bookId) {
        try {
            Optional<Book> bookOptional = bookRepository.findById(bookId);
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                return book.getStatus(); // Devuelve el estado del libro (true si está activo)
            } else {
                throw new RuntimeException("Book not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error verifying book status", e);
        }
    }


}
