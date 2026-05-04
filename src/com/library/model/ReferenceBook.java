package com.library.model;

public class ReferenceBook extends Book {
    private String status;
    public ReferenceBook(String title, String author, int id, String status) {
        super(title,author,id);
        this.status = status;
    }
    public ReferenceBook(String title,String author, String status) {
        super(title,author);
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    @Override
    public void displayInfo() {
        System.out.println("ID: "+ getId() + " | Name: " + getTitle() + " | Author: " + getAuthor() + " | Status: " + getStatus());
    }
}
