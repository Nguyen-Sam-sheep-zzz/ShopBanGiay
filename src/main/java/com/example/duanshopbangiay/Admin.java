package com.example.duanshopbangiay;

public class Admin extends User {
    private String role = "admin";
    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }
}