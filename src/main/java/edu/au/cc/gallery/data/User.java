package edu.au.cc.gallery.data;

public class User {
    private String username;
    private String password;
    private String full_name;

    public User() {

    }
    public User(String username, String password, String full_name) {
        this.username = username;
        this.password = password;
        this.full_name = full_name;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }



    
}

