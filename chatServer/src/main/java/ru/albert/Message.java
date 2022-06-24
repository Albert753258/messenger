package ru.albert;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.ByteArrayOutputStream;

public class Message {
    public long id;
    public String action;
    public long authorId;
    public String text;
    public long timeInMillis;
    public boolean edited;
    public String passHash;
    public String userName;
    public String sessionHash;
    public String email;

    public Message(long id, String action, long authorId, String text, long timeInMillis, boolean edited) {
        this.id = id;
        this.action = action;
        this.authorId = authorId;
        this.text = text;
        this.timeInMillis = timeInMillis;
        this.edited = edited;
    }

    public Message(){

    }
    public Message(String userName, String action){
        this.userName = userName;
        this.action = action;
    }
    public Message(long id, String action, long authorId, String text, long timeInMillis, boolean edited, String passHash, String userName) {
        this.id = id;
        this.action = action;
        this.authorId = authorId;
        this.text = text;
        this.timeInMillis = timeInMillis;
        this.edited = edited;
    }
    public Message(int id, int authorId, String text, long timeInMillis, boolean edited) {
        this.id = id;
        this.authorId = authorId;
        this.text = text;
        this.timeInMillis = timeInMillis;
        this.edited = edited;
    }
    public Message(String action){
        this.action = action;
    }
    public Message(String action, long id){
        this.action = action;
        this.id = id;
    }

    @Override
    public String toString() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            JsonFactory jfactory = new JsonFactory();
            JsonGenerator jGenerator = jfactory
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
            jGenerator.writeStringField("email", email);
            jGenerator.writeEndObject();
            jGenerator.close();
            String json = stream.toString();
            //System.out.println(json);
            return json;
        }
        catch (Exception e){
            return null;
        }
    }
}