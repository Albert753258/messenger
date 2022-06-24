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
    public static LinkedList<Chat> chats = new LinkedList<>();
    Random random = new Random();
    public static long lastChatId;
    public static int clientCount = 0;
    BufferedWriter accountWriter = new BufferedWriter(new FileWriter("authFile", true));

    public ServerEndpoint() throws IOException {
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println(format("%s joined the chat room.", session.getId()));
        //synchronized (sessions){
            //sessions.add(session);
        //}
        //peers.add(session);
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
    public void onMessage(Message message, Session session) throws IOException, EncodeException, NoSuchAlgorithmException, InterruptedException {
        if(message.action.equals("get")){
            System.out.println("get");
            //todo remove
        }
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
        if(message.action.equals("newChat")){
            synchronized (chats){
                for(Chat chat: chats){
                    if(chat.session1.getId().equals(session.getId()) || chat.session2.getId().equals(session.getId())){
                        sessions.add(chat.session1);
                        sessions.add(chat.session2);
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
            sessions.add(session);
            clientCount++;
            startChat();
            System.out.println("Register success");
        }
        else if(message.action.equals("login")){
            String passHash = message.passHash;
            boolean userExist = false;
            for(Account account : Main.accounts){
                if(account.passHash.equals(passHash)){
                    userExist = true;
                    //todo выдача нового хэша сессии при логине и редактирование файла
                    //MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    //messageDigest.update((message.userName + passHash + random.nextInt()).getBytes());
                    //String sessionHash = bytesToHex(messageDigest.digest());
                    Message loginMessage = new Message();
                    loginMessage.action = "loginhash";
                    loginMessage.text = account.sessionHash;
                    loginMessage.userName = message.userName;
                    sessions.add(session);
                    clientCount++;
                    startChat();
                    session.getBasicRemote().sendObject(loginMessage.toString());
                }
            }
            if(!userExist){
                Message loginInvalidMessage = new Message();
                loginInvalidMessage.action = "logininvalid";
                session.getBasicRemote().sendObject(loginInvalidMessage.toString());
            }
        }
        else if(message.action.equals("sessionHashCheck")){
            for(Account account: Main.accounts){
                if(account.sessionHash.equals(message.sessionHash)){
                    sessions.add(session);
                    session.getBasicRemote().sendText(new Message("sessionValid").toString());
                    clientCount++;
                    startChat();
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
            }
        }
        //peers.remove(session);
    }
    public void startChat() throws IOException, InterruptedException {
        System.out.println("Trying to start...");
        if(ServerEndpoint.clientCount >= 2){
            System.out.println("Starting...");
            Session session1;
            Session session2;
            synchronized (sessions){
                session1 = ServerEndpoint.sessions.get(random.nextInt(ServerEndpoint.clientCount - 1));
                sessions.remove(session1);
                session2 = ServerEndpoint.sessions.get(random.nextInt(ServerEndpoint.clientCount - 1));
                sessions.remove(session2);
            }
            synchronized (chats){
                chats.add(new Chat(session1, session2));
            }
            //todo когда много клиентов начинается какое-то гавно
            session1.getBasicRemote().sendText(new Message("chatFound", lastChatId).toString());
            session2.getBasicRemote().sendText(new Message("chatFound", lastChatId).toString());
            lastChatId++;
            System.out.println("Started");
        }
    }
}