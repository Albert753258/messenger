package ru.albert;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainTest {
    public static Connection connection;
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/messenger";
    public static final String DB_USER = "albert";
    public static final String DB_PASSWORD = "1";
    public static final String GET_MESSAGES = "select * from messages";
    public static void main(String[] args) throws IOException, SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
