package ru.albert.quich;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.DeploymentException;
import javax.websocket.Session;

public class TurboSession{
    public static void sendMessage(Message message) throws IOException, URISyntaxException, DeploymentException {
        if(StartActivity.session.isOpen()){
            StartActivity.session.getBasicRemote().sendText(message.toString());
        }
        else {
            StartActivity.session = StartActivity.client.connectToServer(ClientEndpoint.class, new URI(StartActivity.host));
            System.out.println("Reopen session");
            sendMessage(message);
        }
    }
}
