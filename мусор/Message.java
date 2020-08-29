package ru.albert;
import java.util.Date;

public class Message {

    public String content;
    public String sender;
    public Date received;

    public Date getReceived() {
        return received;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReceived(Date received) {
        this.received = received;
    }
    // getters and setters
}