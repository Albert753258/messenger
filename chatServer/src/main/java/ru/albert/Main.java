package ru.albert;


import javax.websocket.DecodeException;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static LinkedList<Message> messages = new LinkedList<>();
    public static LinkedList<Account> accounts = new LinkedList<>();
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/messenger";
    public static final String DB_USER = "albert";
    public static final String DB_PASSWORD = "1";
    public static final String GET_ACCOUNTS = "SELECT * from accounts";
    public static final String INSERT_SQL = "INSERT INTO accounts (username, passhash, sessionhash, email) VALUES ";
    public static final String UPDATE_SQL_SESSIONHASH = "UPDATE accounts SET sessionhash = '";
    public static final String UPDATE_SQL_PASSHASH = "UPDATE accounts SET passhash = '";
    public static Connection connection;
    public static Statement statement;
    public static int lastId = 0;
    public static void main(String[] args) throws DeploymentException, IOException, DecodeException, EncodeException, SQLException {
        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("127.0.0.1", 1111, "/", ServerEndpoint.class);
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        statement = connection.createStatement();
        ResultSet result = statement.executeQuery(GET_ACCOUNTS);
        while (result.next()){
            Account account = new Account(result.getString("username"), result.getString("passhash"), result.getString("sessionhash"), result.getString("email"));
            accounts.add(account);
        }
        server.start();
        Scanner sc = new Scanner(System.in);
        sc.nextInt();
    }
}
//fulmen, ignotus, леонардик, quich, ichat, imess, imessage, isend