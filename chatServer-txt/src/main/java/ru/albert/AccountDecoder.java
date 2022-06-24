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

public class AccountDecoder implements Decoder.Text<Account> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public Account decode(final String textMessage) throws DecodeException {
        try {
            JsonFactory jfactory = new JsonFactory();
            JsonParser jParser = jfactory.createParser(textMessage);
            Account account = new Account();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if ("userName".equals(fieldname)) {
                    jParser.nextToken();
                    account.userName = jParser.getText();
                    //builder.text(jParser.getText());
                }
                else if ("passHash".equals(fieldname)) {
                    jParser.nextToken();
                    account.passHash = jParser.getText();
                    //builder.action(jParser.getText());
                }
                else if("sessionHash".equals(fieldname)){
                    jParser.nextToken();
                    account.sessionHash = jParser.getText();
                }
            }
            jParser.close();
            return account;
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