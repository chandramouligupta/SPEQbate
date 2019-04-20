package com.qbate;

public class User {
    private String userid;
    private String email;

    public User() {
    }

    public User(String userid, String email) {
        this.userid = userid;
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
