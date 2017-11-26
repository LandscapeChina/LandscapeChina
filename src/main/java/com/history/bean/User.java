package com.history.bean;

public class User {
    private String tel;
    private String id;
    private String password;
    private String account;
    public User() {
    }

    public User(String tel, String id, String password, String account) {
        this.tel = tel;
        this.id = id;
        this.password = password;
        this.account = account;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
