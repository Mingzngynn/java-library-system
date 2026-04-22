package com.library.model;

public class TextBook extends Book {
    private String subject;
    private String status;
    public TextBook(String title, String author, String subject, int id, String status){
        super(title,author,id);
        this.subject = subject;
    }
    public TextBook(String title, String author, String subject, String status){
        super(title,author);
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String Newstatus) {
        this.status = Newstatus;
    }
    public String getSubject() {
        return this.subject;
    }
    public void setSubject(String Newsubject) {
        this.subject = Newsubject;
    }
    @Override
    public void displayInfo() {
        System.out.println("ID: "+ getId() + " | Name: " + getTitle() + " | Author: " + getAuthor() + " | Subject: " + getSubject());
    }
}
