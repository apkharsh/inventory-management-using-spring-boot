package com.apkharsh.inventory.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection="books")
public class Book {
    @Id
    private String ID;
    private String name;
    private String genre;
    private String author;
    private int yearPublished;

    private int quantity;
    public Book(){
        super();
        this.quantity = 1;
    }

    public Book(String name, String genre, String author, int yearPublished){
        this.ID = UUID.randomUUID().toString();
        this.name = name;
        this.genre = genre;
        this.author = author;
        this.yearPublished = yearPublished;
        this.quantity = 1;
    }

    public Book(String ID, String name, String genre, String author, int yearPublished){
        this.ID = ID;
        this.name = name;
        this.genre = genre;
        this.author = author;
        this.yearPublished = yearPublished;
        this.quantity = 1;
    }

    public Book(String ID, String name, String genre, String author, int yearPublished, int quantity){
        this.ID = ID;
        this.name = name;
        this.genre = genre;
        this.author = author;
        this.yearPublished = yearPublished;
        this.quantity = quantity;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getID() { return ID; }

    public void setID(String ID) { this.ID = ID; }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public int getYearPublished() { return yearPublished; }

    public void setYearPublished(int yearPublished) { this.yearPublished = yearPublished; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }


    @Override
    public String toString() {
        return "Book{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", author='" + author + '\'' +
                ", yearPublished=" + yearPublished +
                '}';
    }
}
