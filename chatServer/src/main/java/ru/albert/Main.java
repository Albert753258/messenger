package ru.albert;


import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ru.albert.ServerEndpoint.chats;
import static ru.albert.ServerEndpoint.sessions;

public class Main {
    public static LinkedList<Message> messages = new LinkedList<>();
    public static LinkedList<Account> accounts = new LinkedList<>();

    public static Connection connection;
    public static Statement statement;
    public static SSLSender sender;
    public static int lastId = 0;
    public static void main(String[] args) throws DeploymentException, SQLException {
        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("172.86.75.11", 1111, "/", ServerEndpoint.class);
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
                synchronized (sessions){
                    for(Session session : sessions){
                        if(!session.isOpen()){
                            sessions.remove(session);
                            ServerEndpoint.clientCount--;
                            System.out.println("Session removed" + session.getId());
                            break;
                        }
                    }
                    synchronized (chats){
                        try {
                            for (Chat chat : chats) {
                                if (!chat.session1.isOpen()) {
                                    sessions.add(chat.session2);
                                    chat.session2.getBasicRemote().sendText(new Message("stopChat").toString());
                                    chats.remove(chat);
                                    break;
                                }
                                else if (!chat.session2.isOpen()) {
                                    sessions.add(chat.session1);
                                    chat.session1.getBasicRemote().sendText(new Message("stopChat").toString());
                                    chats.remove(chat);
                                    break;
                                }
                                ServerEndpoint.clientCount++;
                            }
                        }
                        catch (Exception e){}
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
        server.start();
        Scanner sc = new Scanner(System.in);
        sc.nextInt();
    }
}
//fulmen, ignotus, леонардик, quich, ichat, imess, imessage, isend