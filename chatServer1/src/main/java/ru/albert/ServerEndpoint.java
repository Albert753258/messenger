package ru.albert;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

@javax.websocket.server.ServerEndpoint(value = "/chat", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ServerEndpoint {
    static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(format("%s joined the chat room.", session.getId()));
        peers.add(session);
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException {
        if(message.action.equals("get")){
            System.out.println("get");
            for(Message message1: Main.messages){
                session.getBasicRemote().sendObject(message1.toString());
            }
        }
        else if(message.action.equals("send")){
//            BufferedWriter writer = new BufferedWriter(new FileWriter("file", true));
//            writer.write(message.toString() + "\n");
//            writer.close();
            String SQL = Main.INSERT_SQL + "('send', " + message.authorId + ", '" + message.text + "', " + message.timeInMillis + ", " + message.edited + ")";
            try {
                Main.statement.executeUpdate(SQL);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("send");
            Main.messages.add(message);
            for (Session peer : peers) {
                peer.getBasicRemote().sendText(message.toString());
            }
//            session.getBasicRemote().sendText(message.toString());
        }
//        else if(message.action.equals("send")){
//            Main.messages.add(message);
//            for (Session peer : peers) {
//                if (!session.getId().equals(peer.getId())) { // do not resend the message to its sender
//                    peer.getBasicRemote().sendObject(message);
//                }
//            }
//            System.out.println("send");
//        }
//        else if(message.text.equals("getid")){
//            System.out.println("getid");
//        }
        //broadcast the message
//        for (Session peer : peers) {
//            if (!session.getId().equals(peer.getId())) { // do not resend the message to its sender
//                peer.getBasicRemote().sendObject(message);
//            }
//        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        System.out.println(format("%s left the chat room.", session.getId()));
        peers.remove(session);
    }
}