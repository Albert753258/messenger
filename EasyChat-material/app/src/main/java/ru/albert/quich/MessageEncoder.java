package ru.albert.quich;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import javax.websocket.Encoder;

import javax.websocket.EndpointConfig;
import java.io.ByteArrayOutputStream;

public class MessageEncoder implements Encoder.Text<Message> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(final Message message){
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            JsonFactory jFactory = new JsonFactory();
            JsonGenerator jGenerator = jFactory
                    .createGenerator(stream, JsonEncoding.UTF8);
            jGenerator.writeStartObject();
            jGenerator.writeNumberField("id", message.id);
            jGenerator.writeNumberField("authorId", message.authorId);
            jGenerator.writeStringField("text", message.text);
            jGenerator.writeNumberField("timeMillis", message.timeInMillis);
            jGenerator.writeBooleanField("edited", message.edited);
            jGenerator.writeEndObject();
            jGenerator.close();
            String json = stream.toString();
            System.out.println(json);
            return json;
        }
        catch (Exception e){
            return null;
        }
    }
}