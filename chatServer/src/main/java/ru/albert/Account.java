package ru.albert;

public class Account {
    public String userName;
    public String passHash;
    public String sessionHash;
    public String email;
    public int verified;
    public Account(String userName, String passHash, String sessionHash, String email, int verified){
        this.userName = userName;
        this.passHash = passHash;
        this.sessionHash = sessionHash;
        this.email = email;
        this.verified = verified;
    }
    public Account(){

    }
}
