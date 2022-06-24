package ru.albert;

public class Account {
    public String userName;
    public String passHash;
    public String sessionHash;
    public String email;
    public Account(String userName, String passHash, String sessionHash, String email){
        this.userName = userName;
        this.passHash = passHash;
        this.sessionHash = sessionHash;
        this.email = email;
    }
    public Account(){

    }
}
