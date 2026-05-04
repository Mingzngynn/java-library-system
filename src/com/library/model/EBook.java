package com.library.model;

public class EBook extends Book {
    private double fileSize;
    public EBook(String title, String author, int id, double fileSize) {
        super(title, author, id);
        this.fileSize = fileSize;
    }
    public EBook(String title, String author, double fileSize) {
        super(title, author);
        this.fileSize = fileSize;
    }
    public double getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(double newfileSize) {
        this.fileSize = newfileSize;
    }
    @Override
    public void displayInfo() {
        System.out.println("ID: "+ getId()+ " | Name: " + getTitle() + " | Author: "+ getAuthor()+ " | File size: " + getFileSize());
    }
}
