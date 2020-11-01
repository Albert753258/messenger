package ru.albert.easychat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.DeploymentException;
import javax.websocket.Session;


public class MainActivity3 extends AppCompatActivity {
    //public static TextView chatText;
    public static EditText messageText;
    public String host = "ws://192.168.43.244:1111/chat";
    public static Context context;
    //public int port = 1111;
    public static AppCompatActivity activity;
    public Session session;
    public static TableLayout tl;
    public static ScrollView scrollView;
    //public static int lastId = 0;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.mainactivity2);
//        TableLayout tl = new TableLayout(this);
//        tl.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
//        RelativeLayout linearLayout = new RelativeLayout(this);
//        linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//        scrollView = findViewById(R.id.scrollView);
//        TextView textView = new TextView(this);
//        TextView textView1 = new TextView(this);
//        textView1.setText("fvdv");
//        textView.setText("message.text");
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        textView.setLayoutParams(params);
//        textView1.setLayoutParams(params);
//        scrollView.addView(tl);
//        tl.addView(textView1);
//        tl.addView(textView);
        try {
            setContentView(R.layout.mainactivity2);
            messageText = findViewById(R.id.messageText);
            scrollView = findViewById(R.id.scrollView);
            context = this;
            tl = new TableLayout(this);
            tl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            scrollView.addView(tl);
            //chatText = findViewById(R.id.chatText);
            //chatText.setMovementMethod(new ScrollingMovementMethod());
            //chatText.setLineSpacing(5, 20);
            activity = this;
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

            Button button = findViewById(R.id.sendButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //scrollView.fullScroll(View.FOCUS_DOWN);
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
                }
            });
            firstMessageReceiver downloader1 = new firstMessageReceiver();
            downloader1.execute();
            int o = 0;
        }
        catch (ClassCastException e){
            e.printStackTrace();
        }
    }
}