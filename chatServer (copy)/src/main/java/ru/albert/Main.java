package ru.albert;


import javax.websocket.DecodeException;
import javax.websocket.DeploymentException;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static LinkedList<Message> messages = new LinkedList<>();
    public static LinkedList<Account> accounts = new LinkedList<>();
    public static LinkedList<Account> queque;
    public static int lastId = 0;
    public static void main(String[] args) throws DeploymentException, IOException, DecodeException {
        //org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("127.0.0.1", 1111, "/", ServerEndpoint.class);

            BufferedReader messagesReader = new BufferedReader(new FileReader("file"));
            BufferedReader accountsReader = new BufferedReader(new FileReader("authFile"));
            String message = messagesReader.readLine();
            String accountStr = accountsReader.readLine();
            MessageDecoder decoder = new MessageDecoder();
            AccountDecoder accountDecoder = new AccountDecoder();
            while (!(message == null)){
                messages.add(decoder.decode(message));
                message = messagesReader.readLine();
            }
            while (accountStr != null){
                accounts.add(accountDecoder.decode(accountStr));
                accountStr = accountsReader.readLine();
            }
            server.start();
        //Scanner sc = new Scanner(System.in);
        while (true){
            if(ServerEndpoint.clientCount >= 2){
                ServerEndpoint.sessions.get(0).getBasicRemote().sendText(new Message("conncheck").toString());
            }
        }
    }
}
//fulmen, ignotus, леонардик, quicha, ichat, imess, imessage, isend