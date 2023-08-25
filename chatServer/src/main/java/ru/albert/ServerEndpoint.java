package ru.albert;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.websocket.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.*;

import static java.lang.String.format;

@javax.websocket.server.ServerEndpoint(value = "/chat", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ServerEndpoint {
    //static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    static LinkedList<Session> sessions = new LinkedList<>();
    public static LinkedList<Chat> chats = new LinkedList<>();
    Random random = new Random();
    public static long lastChatId;
    public static int clientCount = 0;

    public ServerEndpoint() throws IOException {
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println(format("%s joined the chat room.", session.getId()));
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
    public void onMessage(Message message, Session session) throws IOException, EncodeException, NoSuchAlgorithmException, InterruptedException, SQLException {
        if(message.action.equals("send")){
            synchronized (chats){
                for(Chat chat: chats){
                    if(chat.session1.getId().equals(session.getId()) || chat.session2.getId().equals(session.getId())){
                        chat.session1.getBasicRemote().sendText(message.toString());
                        chat.session2.getBasicRemote().sendText(message.toString());
                    }
                }
            }
        }
        else if(message.action.equals("active")){
            session.setMaxIdleTimeout(15000);
        }
        else if(message.action.equals("newChat")){
            synchronized (chats){
                for(Chat chat: chats){
                    if(chat.session1.getId().equals(session.getId()) || chat.session2.getId().equals(session.getId())){
                        sessions.add(chat.session1);
                        sessions.add(chat.session2);
                        chats.remove(chat);
                        clientCount += 2;
                        startChat();
                        //todo возможено, что при малом числе клиентов будут общаться одни и те же
                    }
                }
            }
        }
        else if(message.action.equals("register")){
            String passHash = message.passHash;
            String userName = message.userName;
            boolean isCorrect = true;
            for(Account account: Main.accounts){
                if(account.userName.equals(userName)){
                    isCorrect = false;
                    session.getBasicRemote().sendText(new Message("usernameexist").toString());
                }
                if(account.email.equals(message.email)){
                    isCorrect = false;
                    session.getBasicRemote().sendText(new Message("emailexist").toString());
                }
            }
            if(isCorrect){
                String email = message.email;
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update((userName + passHash + random.nextInt()).getBytes());
                String sessionHash = bytesToHex(messageDigest.digest());
                Random random1 = new Random();
                int code = random1.nextInt(9999 - 1000 + 1) + 1000;
                String SQL = Values.INSERT_SQL + "('" + userName + "', '" + passHash + "', '"  + sessionHash + "', '" + email + "', " +code + ");";
                Main.statement.execute(SQL);
                Message loginMessage = new Message();
                loginMessage.action = "loginhash";
                Main.sender.send("QuiCh email check", code + "", email);
                //todo
                Main.accounts.add(new Account(userName, passHash, sessionHash, email, code));
                //loginMessage.text = sessionHash;
                //loginMessage.userName = userName;
                //session.getBasicRemote().sendObject(loginMessage.toString());
                //sessio ns.add(session);
                //clientCount++;
                //startChat();
                Message message1 = new Message("confirmEmail");
                message1.sessionHash = sessionHash;
                session.getBasicRemote().sendText(message1.toString());
                session.setMaxIdleTimeout(30000);
                System.out.println("Register success");
            }
        }
        else if(message.action.equals("login")){
            String passHash = message.passHash;
            boolean userExist = false;
            for(int i = 0; i < Main.accounts.size(); i++){
                Account account = Main.accounts.get(i);
                if(account.userName.equals(message.userName) && account.passHash.equals(passHash)){
                    userExist = true;
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    messageDigest.update((message.userName + passHash + random.nextInt()).getBytes());
                    String sessionHash = bytesToHex(messageDigest.digest());
                    account.sessionHash = sessionHash;
                    String SQL = Values.UPDATE_SQL_SESSIONHASH + sessionHash + "' WHERE username = '" + account.userName + "';";
                    Main.statement.executeUpdate(SQL);
                    Main.accounts.set(i, account);
                    Message loginMessage = new Message();
                    loginMessage.action = "loginhash";
                    loginMessage.text = account.sessionHash;
                    loginMessage.userName = message.userName;
                    session.getBasicRemote().sendObject(loginMessage.toString());
                    if(account.verified == 0){
                        sessions.add(session);
                        clientCount++;
                        startChat();
                        session.setMaxIdleTimeout(30000);
                        return;
                    }
                    else{
                        Message message1 = new Message("confirmEmail");
                        message1.sessionHash = sessionHash;
                        session.getBasicRemote().sendText(message1.toString());
                    }
                }
            }
            if(!userExist){
                Message loginInvalidMessage = new Message();
                loginInvalidMessage.action = "logininvalid";
                session.getBasicRemote().sendObject(loginInvalidMessage.toString());
            }
        }
        else if(message.action.equals("sessionHashCheck")){
            System.out.println("SessionHashCheckBegin " + Main.accounts.size());
            boolean sessionInvalid = true;
            for(Account account: Main.accounts){
                if(account.sessionHash.equals(message.sessionHash)){
                    if(account.verified == 0){
                        System.out.println("SessionValid");
                        sessions.add(session);
                        sessionInvalid = false;
                        session.getBasicRemote().sendText(new Message("sessionValid").toString());
                        clientCount++;
                        startChat();
                        session.setMaxIdleTimeout(30000);
                    }
                    else {
                        Message message1 = new Message("confirmEmail");
                        sessionInvalid = false;
                        message1.sessionHash = account.sessionHash;
                        System.out.println("confirmEmail");
                        session.getBasicRemote().sendText(message1.toString());
                    }
                }
            }
            if(sessionInvalid){
                System.out.println("SessionInvalid");
                session.getBasicRemote().sendText(new Message("sessionInvalid").toString());
            }
        }
        else if(message.action.equals("emailConfirmCheck")){
            synchronized (Main.accounts){
                for(int i = 0; i < Main.accounts.size(); i++){
                    Account account = Main.accounts.get(i);
                    if(account.sessionHash.equals(message.sessionHash)){
                        if(account.verified == message.authorId){
                            sessions.add(session);
                            session.getBasicRemote().sendText(new Message("emailConfirmOk").toString());
                            String SQL = Values.UPDATE_SQL_VERIFIED + 0 + "' WHERE username = '" + account.userName + "';";
                            Main.statement.executeUpdate(SQL);
                            account.verified = 0;
                            Main.accounts.set(i, account);
                            clientCount++;
                            startChat();
                            session.setMaxIdleTimeout(30000);
                        }
                        else if(account.verified == 0){
                            sessions.add(session);
                            session.getBasicRemote().sendText(new Message("emailConfirmOk").toString());
                            clientCount++;
                            startChat();
                            session.setMaxIdleTimeout(30000);
                        }
                        else {
                            session.getBasicRemote().sendText(new Message("emailConfirmInvalid").toString());
                        }
                        return;
                    }
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        System.out.println(format("%s left the chat room.", session.getId()));
        synchronized (sessions){
            sessions.remove(session);
        }
        synchronized (chats){
            for(Chat chat: chats){
                //todo comments
                if(chat.session1.getId().equals(session.getId())){
                    //chats.remove(chat);
                    sessions.add(chat.session2);
                    //break;
                }
                if(chat.session2.getId().equals(session.getId())){
                    //chats.remove(chat);
                    sessions.add(chat.session1);
                    //break;
                }
                clientCount++;
            }
        }
        //peers.remove(session);
    }
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    public void startChat() throws IOException {
        System.out.println("Trying to start...");
        if(ServerEndpoint.clientCount >= 2){
            System.out.println("Starting...");
            Session session1;
            Session session2;
            synchronized (sessions){
                session1 = ServerEndpoint.sessions.get(random.nextInt(ServerEndpoint.clientCount - 1));
                sessions.remove(session1);
                clientCount--;
                session2 = ServerEndpoint.sessions.get(random.nextInt(ServerEndpoint.clientCount));
                if(session1.getId().equals(session2.getId())){
                    System.out.println("Same sessions");
                    return;
                }
                sessions.remove(session2);
                clientCount--;
            }
            synchronized (chats){
                chats.add(new Chat(session1, session2));
            }
            session1.getBasicRemote().sendText(new Message("chatFound", session1.getId(), session2.getId()).toString());
            session2.getBasicRemote().sendText(new Message("chatFound", session1.getId(), session2.getId()).toString());
            System.out.println("Started");
        }
    }
}