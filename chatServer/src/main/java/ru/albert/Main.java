package ru.albert;


import javax.websocket.DecodeException;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static LinkedList<Message> messages = new LinkedList<>();
    public static LinkedList<Account> accounts = new LinkedList<>();

    public static Connection connection;
    public static Statement statement;
    public static SSLSender sender;
    public static int lastId = 0;
    public static void main(String[] args) throws DeploymentException, IOException, DecodeException, EncodeException, SQLException {
        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("127.0.0.1", 1111, "/", ServerEndpoint.class);
        connection = DriverManager.getConnection(Values.DB_URL, Values.DB_USER, Values.DB_PASSWORD);
        statement = connection.createStatement();
        ResultSet result = statement.executeQuery(Values.GET_ACCOUNTS);
        while (result.next()){
            Account account = new Account(result.getString("username"), result.getString("passhash"), result.getString("sessionhash"), result.getString("email"), result.getInt("verified"));
            accounts.add(account);
        }
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        sender = new SSLSender(Values.EMAIL, Values.MAILPASS);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synchronized (ServerEndpoint.sessions){
                    for(Session session : ServerEndpoint.sessions){
                        if(!session.isOpen()){
                            ServerEndpoint.sessions.remove(session);
                            System.out.println("Session removed" + session.getId());
                        }
                    }
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
        server.start();
        Scanner sc = new Scanner(System.in);
        sc.nextInt();
    }
}
//fulmen, ignotus, леонардик, quich, ichat, imess, imessage, isend