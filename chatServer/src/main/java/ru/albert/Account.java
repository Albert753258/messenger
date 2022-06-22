package ru.albert;

public class Account {
    public String userName;
    public String passHash;
    public String sessionHash;
    public Account(String userName, String passHash, String sessionHash){
        this.userName = userName;
        this.passHash = passHash;
        this.sessionHash = sessionHash;
    }
    public Account(){

    }
}
