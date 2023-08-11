package com.apkharsh.inventory.utils;

import com.apkharsh.inventory.models.Book;

public class RequestWrapper {
    private Book book;
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}