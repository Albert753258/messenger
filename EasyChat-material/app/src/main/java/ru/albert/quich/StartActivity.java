package ru.albert.quich;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.glassfish.tyrus.client.ClientManager;
import java.net.URI;
import javax.websocket.Session;

public class StartActivity extends AppCompatActivity {
    public static String host = "ws://172.86.75.11:1111/chat";
    public static Session session;
    public static SharedPreferences loginSettings;
    public static String userName;
    public static SharedPreferences.Editor loginEditor;
    public static String sessionHash;
    public static int verified;
    public static AppCompatActivity startActivity;
    public static ClientManager client;
    //todo register: hidden email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity = this;
        loginSettings = getSharedPreferences("loginInfo", MODE_PRIVATE);
        loginEditor = loginSettings.edit();
        boolean isFirstLaunch = loginSettings.getString("loginhash", "").equals("");
        System.out.println("Startup");
        Log.println(Log.ASSERT, "Startup", "Startup");
        //boolean isFirstLaunch = true;
        class ServerConnector extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    client = ClientManager.createClient();
                    session = client.connectToServer(ClientEndpoint.class, new URI(host));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }
        }
        if(!isFirstLaunch){
            ServerConnector connector = new ServerConnector();
            connector.execute();
            userName = loginSettings.getString("username", "");
            sessionHash = loginSettings.getString("loginhash", "");
            verified = loginSettings.getInt("verified", 100);
            if(verified != 0){
                Intent intent = new Intent(this, EmailConfirmActivity.class);
                startActivity(intent);
            }
            else{
                class SessionChecker extends AsyncTask<Void, Void, Void> {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            TurboSession.sendMessage(new Message("sessionHashCheck", sessionHash));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                    }
                }
                SessionChecker checker = new SessionChecker();
                checker.execute();
            }
        }
        else {
            setContentView(R.layout.fragment_container);
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new StartFragment())
                        .commit();
            }

            ServerConnector connector = new ServerConnector();
            connector.execute();
        }
    }
}