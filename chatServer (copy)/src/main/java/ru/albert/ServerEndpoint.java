package ru.albert;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import javax.websocket.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static java.lang.String.format;

@javax.websocket.server.ServerEndpoint(value = "/chat", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ServerEndpoint {
    //static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    static LinkedList<Session> sessions = new LinkedList<>();
    Random random = new Random();
    public static int clientCount = 0;
    BufferedWriter accountWriter = new BufferedWriter(new FileWriter("authFile", true));
    BufferedWriter messageWriter = new BufferedWriter(new FileWriter("file", true));

    public ServerEndpoint() throws IOException {
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(format("%s joined the chat room.", session.getId()));
        synchronized (sessions){
            sessions.add(session);
        }
        //peers.add(session);
        clientCount++;
    }
    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException, NoSuchAlgorithmException {
        if(message.action.equals("get")){
            System.out.println("get");
            for(Message message1: Main.messages){
                session.getBasicRemote().sendObject(message1.toString());
            }
        }
        if(message.action.equals("send")){

            messageWriter.write(message.toString() + "\n");
            messageWriter.flush();
            System.out.println("send");
            Main.messages.add(message);
            synchronized (sessions){
                for (Session peer : sessions) {
                    peer.getBasicRemote().sendText(message.toString());
                }
            }
        }
        else if(message.action.equals("register")){
            String passHash = message.passHash;
            String userName = message.userName;
            String email = message.email;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update((userName + passHash + random.nextInt()).getBytes());
            String sessionHash = bytesToHex(messageDigest.digest());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            JsonFactory jFactory = new JsonFactory();
            JsonGenerator jGenerator = jFactory.createGenerator(stream);
            jGenerator.writeStartObject();
            jGenerator.writeStringField("userName", userName);
            jGenerator.writeStringField("passHash", passHash);
            jGenerator.writeStringField("sessionHash", sessionHash);
            jGenerator.writeEndObject();
            jGenerator.close();
            String json = stream.toString();
            accountWriter.write(json + "\n");
            accountWriter.flush();
            Message loginMessage = new Message();
            loginMessage.action = "loginhash";
            loginMessage.text = sessionHash;
            loginMessage.userName = userName;
            session.getBasicRemote().sendObject(loginMessage.toString());
            System.out.println("Register success");
        }
        else if(message.action.equals("queque")){

        }
        else if(message.action.equals("login")){
            String passHash = message.passHash;
            boolean userExist = false;
            for(Account account : Main.accounts){
                if(account.passHash.equals(passHash)){
                    userExist = true;
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    messageDigest.update((message.userName + passHash + random.nextInt()).getBytes());
                    String sessionHash = bytesToHex(messageDigest.digest());
                    Message loginMessage = new Message();
                    loginMessage.action = "loginhash";
                    loginMessage.text = sessionHash;
                    loginMessage.userName = message.userName;
                    session.getBasicRemote().sendObject(loginMessage.toString());
                }
            }
            if(!userExist){
                Message loginInvalidMessage = new Message();
                loginInvalidMessage.action = "logininvalid";
                session.getBasicRemote().sendObject(loginInvalidMessage.toString());
            }
        }
        /*else if(message.action.equals("loginhashcheck")){
            for(Message message1: Main.messages){
                session.getBasicRemote().sendObject(message1.toString());
            }
        }*/
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
        synchronized (sessions){
            sessions.remove(session);
        }
        //peers.remove(session);
    }
}