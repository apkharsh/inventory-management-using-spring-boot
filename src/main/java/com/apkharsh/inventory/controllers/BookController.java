package com.apkharsh.inventory.controllers;

import com.apkharsh.inventory.models.Book;
import com.apkharsh.inventory.utils.RequestWrapper;
import com.apkharsh.inventory.service.BookService;
import com.apkharsh.inventory.service.UserService;
import com.apkharsh.inventory.utils.CustomResponse;
import com.apkharsh.inventory.utils.UserQuantityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    public BookService bookService;

    @Autowired
    public UserService userService;

    @GetMapping("/books/all")
    public ResponseEntity<?> getAllBooks(){

        List<Book> books = bookService.getAllBooks();

        if(books == null){
            return new ResponseEntity<>(new CustomResponse("No books found"), HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(books, HttpStatus.FOUND);
        }
    }

    @GetMapping("/books/name/{bookName}")
    public ResponseEntity<?> getBookByName(@PathVariable("bookName") String name){

        Book book = bookService.getBookByName(name);

        if(book != null){
            return new ResponseEntity<>(book, HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(new CustomResponse("No book found with this name"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/books/id/{bookID}")
    public ResponseEntity<?> getBookByID(@PathVariable("bookID") String bookID){

        Book book = bookService.getBookById(bookID);

        if(book != null){
            return new ResponseEntity<>(book, HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(new CustomResponse("No book found with this ID"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/books/add")
    public ResponseEntity<?> addBook(@RequestBody RequestWrapper wrapper) {

        Book book = wrapper.getBook();
        String userID = wrapper.getID();

        if(!userService.isAuthorized(userID)) {
            return new ResponseEntity<>(new CustomResponse("You are not authorized to perform this action"), HttpStatus.UNAUTHORIZED);
        }
        if(bookService.addBook(book)){
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(new CustomResponse("This book already exists"), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/books/delete/{bookID}")
    public ResponseEntity<?> deleteBook(@PathVariable("bookID") String bookID, @RequestBody String userID){

        if(!userService.isAuthorized(userID)) {
            return new ResponseEntity<>(new CustomResponse("You are not authorized to perform this action"), HttpStatus.UNAUTHORIZED);
        }

        if(bookService.deleteBook(bookID)){
            return new ResponseEntity<>(new CustomResponse("book with Id " + bookID + " is deleted successfully"), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new CustomResponse("No book found with this ID"), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/books/update")
    public ResponseEntity<?> updateBookDetails(@RequestBody RequestWrapper wrapper){

        Book book = wrapper.getBook();
        String userID = wrapper.getID();

        if(!userService.isAuthorized(userID)) {
            System.out.println("authorised");
            return new ResponseEntity<>(new CustomResponse("You are not authorized to perform this action"), HttpStatus.UNAUTHORIZED);
        }

        if(bookService.updateBookDetails(book)){
            System.out.println("updated");
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        else{
            System.out.println("not updated");
            return new ResponseEntity<>(new CustomResponse("Book already exists with same name"), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/books/{bookID}/update/quantity")
    public ResponseEntity<?> updateBookQuantity(@PathVariable String bookID, @RequestBody UserQuantityRequest request){

        String userID = request.getUserId();
        int quantity = request.getQuantity();

        if(!userService.isAuthorized(userID)) {
            return new ResponseEntity<>(new CustomResponse("You are not authorized to perform this action"), HttpStatus.UNAUTHORIZED);
        }

        if(bookService.updateBookQuantity(bookID, quantity)){
            return new ResponseEntity<>(new CustomResponse("quantity updated successfully"), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new CustomResponse("Book does not exist with this ID"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/books/author/{authorName}")
    public ResponseEntity<?> getBooksByAuthor(@PathVariable("authorName") String authorName){

        List<Book> books = bookService.getBooksByAuthor(authorName);

        if(books == null){
            return new ResponseEntity<>(new CustomResponse("No books found"), HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(books, HttpStatus.FOUND);
        }
    }
}
