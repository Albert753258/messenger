package ru.albert.quich;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity3 extends AppCompatActivity {
    public static EditText messageText;
    public static Context context;
    public static TextView messagesText;
    public static AppCompatActivity activity;
    public static ProgressDialog dialog;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.mainactivity2);
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getString(R.string.findingChat));
            dialog.show();
            messageText = findViewById(R.id.messageText);
            messagesText = findViewById(R.id.messagesTexts);
            messagesText.setMovementMethod(new ScrollingMovementMethod());
            context = this;
            activity = this;

            Button sendButton = findViewById(R.id.sendButton);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(messageText.getText().toString().equals(""))){
                        class messageSender extends AsyncTask<Void, Void, Void> {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    StartActivity.session.getBasicRemote().sendText(new Message(ClientEndpoint.chatID, "send", 0, messageText.getText().toString(), System.currentTimeMillis(), false).toString());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            messageText.setText("");
                                        }
                                    });
                                } catch (IOException | IllegalStateException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void result) {
                                super.onPostExecute(result);
                            }
                        }
                        messageSender messageSender = new messageSender();
                        messageSender.execute();
                    }
                }
            });
            Button nextChatButton = findViewById(R.id.newChatButton);
            nextChatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    class NewChat extends AsyncTask<Void, Void, Void> {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                StartActivity.session.getBasicRemote().sendText(new Message("newChat", "dummy").toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messageText.setText("");
                                    }
                                });
                            } catch (IOException | IllegalStateException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                        }
                    }
                    NewChat downloader1 = new NewChat();
                    downloader1.execute();
                }
            });
        }
        catch (ClassCastException e){
            e.printStackTrace();
        }
    }
}