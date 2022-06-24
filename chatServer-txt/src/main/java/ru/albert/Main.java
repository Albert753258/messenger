package ru.albert;


import org.glassfish.tyrus.websockets.frametypes.PongFrameType;

import javax.websocket.DecodeException;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static LinkedList<Message> messages = new LinkedList<>();
    public static LinkedList<Account> accounts = new LinkedList<>();
    public static int lastId = 0;
    public static void main(String[] args) throws DeploymentException, IOException, DecodeException, EncodeException {
        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("127.0.0.1", 1111, "/", ServerEndpoint.class);
        BufferedReader accountsReader = new BufferedReader(new FileReader("authFile"));
        String accountStr = accountsReader.readLine();
        MessageDecoder decoder = new MessageDecoder();
        AccountDecoder accountDecoder = new AccountDecoder();
        while (accountStr != null){
            accounts.add(accountDecoder.decode(accountStr));
            accountStr = accountsReader.readLine();
        }
            server.start();
        Scanner sc = new Scanner(System.in);
        sc.nextInt();
    }
}
//fulmen, ignotus, леонардик, quich, ichat, imess, imessage, isend