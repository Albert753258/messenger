package ru.albert.easychat;

import javax.websocket.OnMessage;
import java.text.SimpleDateFormat;

@javax.websocket.ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ClientEndpoint {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @OnMessage
    public void onMessage(Message message) {
        MainActivity2.chatText.append(message.text + "\n");
    }
}