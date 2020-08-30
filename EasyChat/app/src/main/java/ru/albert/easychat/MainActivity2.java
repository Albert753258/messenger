package ru.albert.easychat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import org.glassfish.tyrus.client.ClientManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.DeploymentException;
import javax.websocket.Session;


public class MainActivity2 extends AppCompatActivity {
    public static TextView chatText;
    public static EditText messageText;
    public String host = "ws://192.168.43.53:1111/chat";
    public int port = 1111;
    public Session session;
    public static int lastId = 0;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            messageText = findViewById(R.id.messageText);
            chatText = findViewById(R.id.chatText);
//            class firstMessageReceiver extends AsyncTask<Void, Void, Void> {
//                @Override
//                protected Void doInBackground(Void... params) {
//                    try {
//
//                    } catch (DeploymentException e) {
//                        e.printStackTrace();
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//                @Override
//                protected void onPostExecute(Void result) {
//                    super.onPostExecute(result);
//                }
//            }
//            firstMessageReceiver downloader1 = new firstMessageReceiver();
//            downloader1.execute();
            class firstMessageReceiver extends AsyncTask<Void, Void, Void> {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        ClientManager client = ClientManager.createClient();
                        session = client.connectToServer(ClientEndpoint.class, new URI(host));
                        session.getBasicRemote().sendText(new Message("get").toString());
                        messageText.setText("");
                    } catch (DeploymentException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                }
            }
            firstMessageReceiver downloader1 = new firstMessageReceiver();
            downloader1.execute();

//            ClientManager client = ClientManager.createClient();
//            session = client.connectToServer(ClientEndpoint.class, new URI(host));
//            Message message1 = new Message("get");
//            session.getBasicRemote().sendText(message1.toString());
//            int i = 0;
            Button button = findViewById(R.id.sendButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(messageText.getText().toString().equals(""))){
                        class firstMessageReceiver extends AsyncTask<Void, Void, Void> {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    session.getBasicRemote().sendText(new Message(0, "send", 0, messageText.getText().toString(), System.currentTimeMillis(), false).toString());
                                    messageText.setText("");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                catch (IllegalStateException e){
                                    try {
                                        ClientManager client = ClientManager.createClient();
                                        session = client.connectToServer(ClientEndpoint.class, new URI(host));
                                        session.getBasicRemote().sendText(new Message(0, "send", 0, messageText.getText().toString(), System.currentTimeMillis(), false).toString());
                                        messageText.setText("");
                                    } catch (DeploymentException ex) {
                                        ex.printStackTrace();
                                    } catch (URISyntaxException ex) {
                                        ex.printStackTrace();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void result) {
                                super.onPostExecute(result);
                            }
                        }
                        firstMessageReceiver downloader1 = new firstMessageReceiver();
                        downloader1.execute();
                    }
//                    ClientManager client = ClientManager.createClient();
//                    try {
//                        session = client.connectToServer(ClientEndpoint.class, new URI(host));
//                        int i = 0;
//                    } catch (DeploymentException e) {
//                        e.printStackTrace();
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
                }
            });
            int o = 0;
        }
        catch (NetworkOnMainThreadException e){
            e.printStackTrace();
        }
    }
}