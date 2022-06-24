package ru.albert.quich;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

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
            JsonFactory jfactory = new JsonFactory();
            JsonParser jParser = jfactory.createParser(textMessage);
            Message message = new Message();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if ("id".equals(fieldname)) {
                    jParser.nextToken();
                    message.id = jParser.getIntValue();
                }
                else if ("authorId".equals(fieldname)) {
                    jParser.nextToken();
                    message.authorId = jParser.getIntValue();
                }
                else if ("action".equals(fieldname)) {
                    jParser.nextToken();
                    message.action = jParser.getText();
                }
                else if ("text".equals(fieldname)) {
                    jParser.nextToken();
                    message.text = jParser.getText();
                }
                else if ("timeInMillis".equals(fieldname)) {
                    jParser.nextToken();
                    message.timeInMillis = jParser.getIntValue();
                }
                else if ("edited".equals(fieldname)) {
                    jParser.nextToken();
                    message.edited = jParser.getBooleanValue();
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
            }
            jParser.close();
            return message;
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