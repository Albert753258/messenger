package ru.albert;

public class Values {
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/messenger";
    public static final String DB_USER = "albert";
    public static final String DB_PASSWORD = "1";
    public static final String GET_ACCOUNTS = "SELECT * from accounts";
    public static final String INSERT_SQL = "INSERT INTO accounts (username, passhash, sessionhash, email, verified) VALUES ";
    public static final String UPDATE_SQL_SESSIONHASH = "UPDATE accounts SET sessionhash = '";
    public static final String UPDATE_SQL_VERIFIED = "UPDATE accounts SET verified = '";
    public static final String UPDATE_SQL_PASSHASH = "UPDATE accounts SET passhash = '";
    public static final String MAILPASS = "753258Zaika";
    public static final String EMAIL = "quickchat@rambler.ru";
}
