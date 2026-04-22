package com.library.model;

public class User {
    private int borrowLimit;
    private int id;
    private String name;
    private String phone;
    private String role;
    public User(int id, int borrowLimit, String name, String phone, String role) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.borrowLimit = borrowLimit;
    }
    public User( String name, String phone, String role) {
        this.name = name;
        this.phone = phone;
        this.role = role;
    }
    public int getBorrowLimit() {
        return borrowLimit;
    }
    public void setBorrowLimit(int borrowLimit) {this.borrowLimit = borrowLimit;}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {this.role = role;}

}
