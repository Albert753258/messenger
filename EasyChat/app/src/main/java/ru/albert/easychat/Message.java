package ru.albert.easychat;

import android.util.Base64;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Message {
    public int id;
    public String action;
    public int authorId;
    public String text;
    public long timeInMillis;
    public boolean edited;
    public String passHash;
    public String userName;
    public String sessionHash;

    public Message(int id, String action, int authorId, String text, long timeInMillis, boolean edited) {
        this.id = id;
        this.action = action;
        this.authorId = authorId;
        this.text = text;
        this.timeInMillis = timeInMillis;
        this.edited = edited;
    }
    public Message(){

    }
    public Message(String password, String userName, String action) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        this.userName = userName;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.reset();
        messageDigest.update(password.getBytes());
        this.passHash = bytesToHex(messageDigest.digest());
        this.action = action;
    }
    public Message(String sessionHash){
        this.action = "get";
        this.sessionHash = sessionHash;
    }

    @Override
    public String toString() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            JsonFactory jFactory = new JsonFactory();
            JsonGenerator jGenerator = jFactory
                    .createGenerator(stream);
            jGenerator.writeStartObject();
            jGenerator.writeStringField("action", action);
            jGenerator.writeNumberField("id", id);
            jGenerator.writeNumberField("authorId", authorId);
            jGenerator.writeStringField("text", text);
            jGenerator.writeNumberField("timeMillis", timeInMillis);
            jGenerator.writeBooleanField("edited", edited);
            jGenerator.writeStringField("passHash", passHash);
            jGenerator.writeStringField("userName", userName);
            jGenerator.writeStringField("sessionHash", sessionHash);
            jGenerator.writeEndObject();
            jGenerator.close();
            return stream.toString();
        }
        catch (Exception e){
            return "null";
        }
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
}