package ru.albert.easychat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.DeploymentException;
import javax.websocket.Session;

public class StartActivity extends AppCompatActivity {
    public static String host = "ws://192.168.43.132:1111/chat";
    public static Session session;
    public static SharedPreferences loginSettings;
    public static String userName;
    public static SharedPreferences.Editor loginEditor;
    public static String sessionHash;
    public static AppCompatActivity startActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity = this;
        loginSettings = getSharedPreferences("loginInfo", MODE_PRIVATE);
        loginEditor = loginSettings.edit();
        boolean isFirstLaunch = loginSettings.getString("loginhash", "").equals("");
        class ServerConnector extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    ClientManager client = ClientManager.createClient();
                    session = client.connectToServer(ClientEndpoint.class, new URI(host));
                } catch (DeploymentException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
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
            Intent intent = new Intent(StartActivity.this, MainActivity3.class);
            StartActivity.this.startActivity(intent);
            finish();
        }
        else {
            setContentView(R.layout.startscreen);

            ServerConnector connector = new ServerConnector();
            connector.execute();
        }
    }
    public void registerButtonMethod(View v){
        if(v.getId() == R.id.registerButton){
            Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
            StartActivity.this.startActivity(intent);
            finish();
        }
    }
    public void loginButtonMethod(View v){
        if(v.getId() == R.id.loginButton){
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            StartActivity.this.startActivity(intent);
            finish();
        }
    }
}