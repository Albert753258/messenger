package ru.albert;


import lombok.SneakyThrows;

import javax.websocket.DecodeException;
import javax.websocket.DeploymentException;
import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static LinkedList<Message> messages = new LinkedList<>();
    public static int lastId = 0;
    public static Connection connection;
    public static Statement statement;
    public static String DB_URL = "jdbc:postgresql://localhost:5432/messenger";
    public static String DB_USER = "albert";
    public static String DB_PASSWORD = "753258";
    public static String GET_MESSAGES = "select * from messages";
    public static String INSERT_SQL = "insert into messages(action, authorId, messagetext, timeInMillis, edited) values ";
    public static void main(String[] args) throws SQLException, DeploymentException {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        statement = connection.createStatement();
        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("192.168.43.244", 1111, "/", ServerEndpoint.class);
        ResultSet result = statement.executeQuery(GET_MESSAGES);
        //result.next();
        while (result.next()){
            Message message = new Message(result.getLong("id"), result.getString("action"), result.getLong("authorid"),
                    result.getString("messagetext"), result.getLong("timeinmillis"), result.getBoolean("edited"));
            messages.add(message);
        }

        server.start();
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        int i = 0;
//            BufferedReader messagesReader = new BufferedReader(new FileReader("file"));
//            String message = messagesReader.readLine();
//            MessageDecoder decoder = new MessageDecoder();
//            while (!(message == null)){
//                messages.add(decoder.decode(message));
//                message = messagesReader.readLine();
//            }
            //server.start();
    }
}