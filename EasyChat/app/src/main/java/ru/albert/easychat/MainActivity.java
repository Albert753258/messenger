package ru.albert.easychat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public TextView chatText;
    public EditText messageText;
    public Socket client;
    public String host = "192.168.43.253";
    public int port = 1111;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageText = findViewById(R.id.messageText);
        chatText = findViewById(R.id.chatText);
        class firstMessageReceiver extends AsyncTask<Void, Void, Void> {
            public String text;
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    client = new Socket(host, port);
                    PrintWriter toServer = new PrintWriter(client.getOutputStream(), true);
                    toServer.println("get");
                    BufferedReader reader= new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String str = reader.readLine();
                    while (!(str.equals("\f"))){
                        chatText.append(str + "\n");
                        str = reader.readLine();
                    }
                    int i = 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                new Thread(accepter).start();
                super.onPostExecute(result);
            }
        }
        firstMessageReceiver downloader1 = new firstMessageReceiver();
        downloader1.execute();


        Button button = findViewById(R.id.sendButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(messageText.getText().toString());
            }
        });
        int o = 0;
    }
    public void sendMessage(final String text){
        class messageSender extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    client = new Socket(host, port);
                    BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    toServer.write("send " + text);
                    toServer.flush();
                    toServer.close();
                    int i = 0;
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
        messageSender downloader = new messageSender();
        downloader.execute();
    }
    private Runnable accepter = new Runnable() {
        String str;
        public void run() {
            while (true){
                try {
                    client = new Socket(host, port);
                    BufferedReader reader= new BufferedReader(new InputStreamReader(client.getInputStream()));
                    str = reader.readLine();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatText.append(str + "\n");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
