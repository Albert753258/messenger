package ru.albert.easychat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.DeploymentException;


public class MainActivity3 extends AppCompatActivity {
    public static EditText messageText;
    public static Context context;
    public static TextView messagesText;
    public static AppCompatActivity activity;
    //public static TableLayout tl;
    //public static ScrollView scrollView;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.mainactivity2);
            messageText = findViewById(R.id.messageText);
            messagesText = findViewById(R.id.messagesTexts);
            messagesText.setMovementMethod(new ScrollingMovementMethod());
            //scrollView = findViewById(R.id.scrollView);
            context = this;
            //tl = new TableLayout(this);
            //tl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            //scrollView.addView(tl);
            activity = this;
            class firstMessageReceiver extends AsyncTask<Void, Void, Void> {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageText.setText("");
                            }
                        });
                        StartActivity.session.getBasicRemote().sendText(new Message(StartActivity.sessionHash).toString());
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

            Button button = findViewById(R.id.sendButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(messageText.getText().toString().equals(""))){
                        class messageSender extends AsyncTask<Void, Void, Void> {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    StartActivity.session.getBasicRemote().sendText(new Message(0, "send", 0, messageText.getText().toString(), System.currentTimeMillis(), false).toString());
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
                                        StartActivity.session = client.connectToServer(ClientEndpoint.class, new URI(StartActivity.host));
                                        StartActivity.session.getBasicRemote().sendText(new Message(0, "send", 0, messageText.getText().toString(), System.currentTimeMillis(), false).toString());
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
                        messageSender downloader1 = new messageSender();
                        downloader1.execute();
                    }
                }
            });
            firstMessageReceiver downloader1 = new firstMessageReceiver();
            downloader1.execute();
        }
        catch (ClassCastException e){
            e.printStackTrace();
        }
    }
}