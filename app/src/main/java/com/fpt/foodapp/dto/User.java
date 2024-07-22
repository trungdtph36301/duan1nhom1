package com.fpt.foodapp.dto;

public class User {


    private String name, user, pass, phone, isUser;
    double vitien;

    public User() {
    }


    public User(String name, String user, String pass, String phone) {
        this.name = name;
        this.user = user;
        this.pass = pass;
        this.phone = phone;
        this.isUser = "true";
        this.vitien = 0;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }

    public double getVitien() {
        return vitien;
    }

    public void setVitien(double vitien) {
        this.vitien = vitien;
    }
}
