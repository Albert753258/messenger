package ru.albert;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;

@Builder
@NoArgsConstructor
public class Message {
    public long id;
    public String action;
    public long authorId;
    public String text;
    public long timeInMillis;
    public boolean edited;

    public Message(long id, String action, long authorId, String text, long timeInMillis, boolean edited) {
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

    @Override
    public String toString() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            JsonFactory jfactory = new JsonFactory();
            JsonGenerator jGenerator = jfactory
                    .createGenerator(stream, JsonEncoding.UTF8);
            jGenerator.writeStartObject();
            jGenerator.writeStringField("action", action);
            jGenerator.writeNumberField("id", id);
            jGenerator.writeNumberField("authorId", authorId);
            jGenerator.writeStringField("text", text);
            jGenerator.writeNumberField("timeMillis", timeInMillis);
            jGenerator.writeBooleanField("edited", edited);
            jGenerator.writeEndObject();
            jGenerator.close();
            String json = new String(stream.toByteArray(), "UTF-8");
            //System.out.println(json);
            return json;
        }
        catch (Exception e){
            return null;
        }
    }
}
