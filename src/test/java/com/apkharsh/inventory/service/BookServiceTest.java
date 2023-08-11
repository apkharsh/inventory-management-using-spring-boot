package com.apkharsh.inventory.service;

import com.apkharsh.inventory.models.Book;
import com.apkharsh.inventory.repositories.BookRepo;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookService bookService;

    @Nested
    public class addBookTest {

        @Test
        public void testAddBook_ExistingBook() {
            // Arrange
            String existingBookName = "Existing Book";
            int existingQuantity = 10;
            int newQuantity = 5;

            Book existingBook = new Book("12345", existingBookName,"science", "harsh", 2020, existingQuantity);
            Book updatedBook = new Book("12345", existingBookName,"science", "harsh", 2020,  newQuantity + existingQuantity);

            when(bookRepo.findByBookName(existingBookName)).thenReturn(existingBook);

            // Act
            boolean result = bookService.addBook(new Book("12345", existingBookName,"science", "harsh", 2020, newQuantity));

            // Assert
            assertTrue(result);
            verify(bookRepo, times(1)).findByBookName(existingBookName);
        }
        @Test
        public void testAddBook_BookNotExists() {
            // Arrange
            String bookName = "To Kill a Mockingbird";
            int newQuantity = 10;

            Book newBook = new Book();
            newBook.setName(bookName);
            newBook.setQuantity(newQuantity);

            when(bookRepo.findByBookName(bookName)).thenReturn(null);
            when(bookRepo.save(any(Book.class))).thenReturn(newBook);

            // Act
            boolean result = bookService.addBook(newBook);

            // Assert
            assertTrue(result);
            assertEquals(newQuantity, newBook.getQuantity());
            assertNotNull(newBook.getID()); // ID should be generated
            verify(bookRepo, times(1)).save(newBook);
        }

    }

    @Nested
    public class getAllBooks {
        @Test
        public void getAllBooksTest() {

            // Expected list
            Book book_1 = new Book("1", "atomic habits", "self help", "temp", 2023);
            Book book_2 = new Book("1", "atomic habits", "self help", "temp", 2023);

            List<Book> expectedBooksList = new ArrayList<>();
            expectedBooksList.add(book_1);
            expectedBooksList.add(book_2);

            // actual list
            List<Book> databaseList = new ArrayList<>();
            databaseList.add(book_1);
            databaseList.add(book_2);

            when(bookRepo.findAll()).thenReturn(databaseList);

            List<Book> actualList = bookService.getAllBooks();
            assertEquals(expectedBooksList, actualList);
        }

        // empty lists
        @Test
        public void getAllBooksTest_EmptyList() {

            List<Book> expectedBooksList = new ArrayList<>();
            List<Book> databaseList = new ArrayList<>();

            when(bookRepo.findAll()).thenReturn(databaseList);

            List<Book> actualList = bookService.getAllBooks();
            assertEquals(expectedBooksList, actualList);
        }

    }

    @Nested
    public class deleteBookTest {
        @Test
        public void deleteBook_bookExists_Test() {

            Book book_1 = new Book("1", "atomic habits", "self help", "harsh", 2023);
            when(bookRepo.findById(anyString())).thenReturn(Optional.of(book_1));

            boolean actualResult = bookService.deleteBook(book_1.getID());
            assertTrue(actualResult);

            verify(bookRepo, times(1)).findById(anyString());
        }

        @Test
        public void deleteBook_bookNotExists_test() {

            String nonExistingBookID = "456";
            when(bookRepo.findById(nonExistingBookID)).thenReturn(Optional.empty());

            boolean result = bookService.deleteBook(nonExistingBookID);

            assertFalse(result);
            verify(bookRepo, never()).deleteById(nonExistingBookID);
        }

    }

    @Nested
    public class updateBookTest {
        @Test
        public void testUpdateBookDetails_Success() {
            // Arrange
            String bookId = "123";
            String existingBookName = "Existing Book";
            String updatedBookName = "Updated Book";

            Book existingBook = new Book(bookId, existingBookName, "self help", "temp", 2023);
            Book updatedBook = new Book(bookId, updatedBookName, "self help", "temp", 2023);

            when(bookRepo.findById(bookId)).thenReturn(Optional.of(existingBook));
            when(bookRepo.findByBookName(updatedBookName)).thenReturn(null);
            when(bookRepo.save(any(Book.class))).thenReturn(updatedBook);

            // Act
            boolean result = bookService.updateBookDetails(updatedBook);

            // Assert
            assertTrue(result);
            verify(bookRepo, times(2)).findById(bookId);
            verify(bookRepo, times(1)).findByBookName(updatedBookName);
            verify(bookRepo, times(1)).save(updatedBook);
        }

        @Test
        public void testUpdateBookDetails_BookNotExists() {
            // Arrange
            String bookId = "123";
            Book updatedBook = new Book(bookId, "atomic habits", "self help", "temp", 2023);

            when(bookRepo.findById(bookId)).thenReturn(Optional.empty());

            // Act
            boolean result = bookService.updateBookDetails(updatedBook);

            // Assert
            assertFalse(result);
            verify(bookRepo, times(1)).findById(bookId);
            verify(bookRepo, never()).findByBookName(anyString());
            verify(bookRepo, never()).save(any(Book.class));
        }
    }

    @Nested
    public class getBookByNameTest{
        @Test
        public void getBookByNameTest_bookIsPresent() {

            Book book_1 = new Book("1", "atomic habits", "self help", "temp", 2023);
            when(bookRepo.findByBookName("atomic habits")).thenReturn(book_1);

            Book actualResult = bookService.getBookByName("atomic habits");
            assertEquals(book_1, actualResult);
        }

        @Test
        public void getBookByNameTest_bookNotPresent() {

            Book book_1 = new Book("1", "atomic habits", "self help", "temp", 2023);
            when(bookRepo.findByBookName("atomic habits")).thenReturn(null);

            Book actualResult = bookService.getBookByName("atomic habits");
            assertNull(actualResult);
        }

    }

    @Test
    public void getBooksByAuthorNameTest() {

        Book book_1 = new Book("1", "atomic habits", "self help", "harsh", 2023);

        List<Book> expectedList = new ArrayList<>();
        expectedList.add(book_1);

        List<Book> actualList = new ArrayList<>();
        actualList.add(book_1);

        when(bookRepo.findByAuthorName("harsh")).thenReturn(actualList);

        List<Book> actualResult = bookService.getBooksByAuthor("harsh");
        assertEquals(expectedList, actualResult);

    }

}
