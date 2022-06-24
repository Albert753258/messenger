package ru.albert;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

public class MessageDecoder implements Decoder.Text<Message> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public Message decode(final String textMessage) throws DecodeException {
        try {
            //System.out.println(textMessage);
            JsonFactory jfactory = new JsonFactory();
            JsonParser jParser = jfactory.createParser(textMessage);
            Message message = new Message();
            //Message.MessageBuilder builder = new Message.MessageBuilder();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if ("authorId".equals(fieldname)) {
                    jParser.nextToken();
                    message.authorId = jParser.getIntValue();
                    //builder.authorId(jParser.getIntValue());
                }
                else if ("text".equals(fieldname)) {
                    jParser.nextToken();
                    message.text = jParser.getText();
                    //builder.text(jParser.getText());
                }
                else if ("action".equals(fieldname)) {
                    jParser.nextToken();
                    message.action = jParser.getText();
                    //builder.action(jParser.getText());
                }
                else if ("timeInMillis".equals(fieldname)) {
                    jParser.nextToken();
                    message.timeInMillis = jParser.getIntValue();
                    //builder.timeInMillis(jParser.getIntValue());
                }
                else if ("edited".equals(fieldname)) {
                    jParser.nextToken();
                    message.edited = jParser.getBooleanValue();
                    //builder.edited(jParser.getBooleanValue());
                }
                else if("passHash".equals(fieldname)){
                    jParser.nextToken();
                    message.passHash = jParser.getText();
                }
                else if("sessionHash".equals(fieldname)){
                    jParser.nextToken();
                    message.sessionHash = jParser.getText();
                }
                else if("userName".equals(fieldname)){
                    jParser.nextToken();
                    message.userName = jParser.getText();
                }
                else if("email".equals(fieldname)){
                    jParser.nextToken();
                    message.email = jParser.getText();
                }
            }
            Main.lastId++;
            jParser.close();
            return message;
            //return builder.build();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean willDecode(final String s) {
        return true;
    }

}