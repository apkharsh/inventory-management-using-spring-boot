package com.apkharsh.inventory.repositories;

import com.apkharsh.inventory.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends MongoRepository<Book, String> {
    @Query("{ 'name' : ?0 }")
    Book findByBookName(String bookName);

    @Query("{ 'author' : ?0 }")
    List<Book> findByAuthorName(String authorName);
}
