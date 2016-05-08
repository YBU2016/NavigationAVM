package com.example.Database.Entities;

/**
 * Created by omur on 08.05.2016.
 */
public class LoginEntity extends EntityBase {

    private String Name ;
    private String SurName ;
    private String UserName ;
    private String Email ;
    private String Password ;

    public LoginEntity(int id, String name, String surName, String userName, String email, String password) {
        Email = email;
        Name = name;
        Password = password;
        SurName = surName;
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurName() {
        return SurName;
    }

    public void setSurName(String surName) {
        SurName = surName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
