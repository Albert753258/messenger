package ru.albert;


import lombok.SneakyThrows;

import javax.websocket.DecodeException;
import javax.websocket.DeploymentException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static LinkedList<Message> messages = new LinkedList<>();
    public static int lastId = 0;
    public static void main(String[] args) throws IOException, DecodeException {
//        Server server = new Server();
//        server.start(1111);



//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        JsonFactory jfactory = new JsonFactory();
//        JsonGenerator jGenerator = jfactory
//                .createGenerator(stream, JsonEncoding.UTF8);
//        jGenerator.writeStartObject();
//        jGenerator.writeNumberField("id", 1);
//        jGenerator.writeNumberField("authorId", 1);
//        jGenerator.writeStringField("text", "hello");
//        jGenerator.writeNumberField("timeMillis", 10000000L);
//        jGenerator.writeBooleanField("edited", false);
//        jGenerator.writeEndObject();
//        jGenerator.close();
//        String json = new String(stream.toByteArray(), "UTF-8");
//        System.out.println(json);



//        JsonParser jParser = jfactory.createParser(json);
//        while (jParser.nextToken() != JsonToken.END_OBJECT) {
//            String fieldname = jParser.getCurrentName();
//            if ("text".equals(fieldname)) {
//                jParser.nextToken();
//                System.out.println(jParser.getText());
//            }
//        }
//        jParser.close();



//        SSLSender sender = new SSLSender("albert.nasyrov2016@gmail.com", "&753258&");
//        sender.send("ewf", "ewf", "ahhhlbert.nasyrov2016@gmail.com", "lidia.dementyeva@gmail.com");



        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("192.168.43.53", 1111, "/", ServerEndpoint.class);
        try {
            BufferedReader messagesReader = new BufferedReader(new FileReader("file"));
            String message = messagesReader.readLine();
            MessageDecoder decoder = new MessageDecoder();
            while (!(message == null)){
                messages.add(decoder.decode(message));
                message = messagesReader.readLine();
            }
            server.start();
            System.out.println("Press any key to stop the server..");
            new Scanner(System.in).nextLine();
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }
}