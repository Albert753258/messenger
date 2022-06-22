package ru.albert.easychat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.DeploymentException;
import javax.websocket.Session;


public class MainActivity2 extends AppCompatActivity {
    public static TextView chatText;
    public static EditText messageText;
    public String host = "ws://192.168.43.244:1111/chat";
    public int port = 1111;
    public static AppCompatActivity activity;
    public Session session;
    public static ScrollView scrollView;
    public static int lastId = 0;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            messageText = findViewById(R.id.messageText);
            scrollView = findViewById(R.id.scrollView);
            chatText = findViewById(R.id.chatText);
            chatText.setMovementMethod(new ScrollingMovementMethod());
            chatText.setLineSpacing(5, 20);
            activity = this;
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageText.setText("");
                            }
                        });
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

//            ClientManager client = ClientManager.createClient();
//            session = client.connectToServer(ClientEndpoint.class, new URI(host));
//            Message message1 = new Message("get");
//            session.getBasicRemote().sendText(message1.toString());
//            int i = 0;
            Button button = findViewById(R.id.sendButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                    if(!(messageText.getText().toString().equals(""))){
                        class firstMessageReceiver extends AsyncTask<Void, Void, Void> {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    session.getBasicRemote().sendText(new Message(0, "send", 0, messageText.getText().toString(), System.currentTimeMillis(), false).toString());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            messageText.setText("");
                                        }
                                    });
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
            firstMessageReceiver downloader1 = new firstMessageReceiver();
            downloader1.execute();
            int o = 0;
        }
        catch (NetworkOnMainThreadException e){
            e.printStackTrace();
        }
    }
    private void addMessage(String msg) {
        // append the new string
        chatText.append(msg + "\n");
        // find the amount we need to scroll.  This works by
        // asking the TextView internal layout for the position
        // of the final line and then subtracting the TextView height
        final int scrollAmount = chatText.getLayout().getLineTop(chatText.getLineCount()) - chatText.getHeight();
        // if there is no need to scroll, scrollAmount will be <=0
        if (scrollAmount > 0)
            chatText.scrollTo(0, scrollAmount);
        else
            chatText.scrollTo(0, 0);
    }
}