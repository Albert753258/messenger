package ru.albert;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainTest {
    public static void main(String[] args) throws IOException, SQLException {
        SSLSender sslSender = new SSLSender(Values.EMAIL, Values.MAILPASS);
        sslSender.send("drghfgd", "gdrgdr", "albert.nasyrov2016@gmail.com");
    }
}
