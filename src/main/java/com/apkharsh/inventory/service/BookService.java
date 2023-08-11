package com.apkharsh.inventory.service;

import com.apkharsh.inventory.models.Book;
import com.apkharsh.inventory.repositories.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    @Autowired
    private BookRepo bookRepo;

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public Book getBookById(String bookID) {
        return bookRepo.findById(bookID).orElse(null);
    }

    public Book getBookByName(String bookName) {
        return bookRepo.findByBookName(bookName);
    }

    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepo.findByAuthorName(authorName);
    }

    public boolean addBook(Book book) {

        Book bookExists = getBookByName(book.getName());

        if (bookExists != null) {

            int totalQuantity = bookExists.getQuantity() + book.getQuantity();
            book.setQuantity(totalQuantity);
            book.setID(bookExists.getID());
            bookRepo.save(book);

        } else {
            book.setID(UUID.randomUUID().toString());
            bookRepo.save(book);
        }

        return true;
    }

    public boolean deleteBook(String bookID) {

        Optional<Book> optionalBook = bookRepo.findById(bookID);

        if (optionalBook.isPresent()) {
            bookRepo.deleteById(bookID);
            return true;
        }
        return false;
    }

    public boolean updateBookQuantity(String bookID, int quantity) {

        Optional<Book> optionalBook = bookRepo.findById(bookID);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setQuantity(quantity);
            bookRepo.save(book);
            return true;
        }

        return false;
    }

    public boolean updateBookDetails(Book book) {

        Optional<Book> optionalBook = bookRepo.findById(book.getID());

        if (optionalBook.isPresent()) {
            Book bookExists = getBookById(book.getID());

            if (bookExists.getName().equals(book.getName()) || getBookByName(book.getName()) == null) {
                bookRepo.save(book);
                return true;
            }
        }

        return false;
    }

}
