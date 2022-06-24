package ru.albert;

import javax.websocket.Session;

public class Chat {
    public Session session1;
    public Session session2;
    public Chat(Session session1, Session session2){
        this.session1 = session1;
        this.session2 = session2;
    }
}
