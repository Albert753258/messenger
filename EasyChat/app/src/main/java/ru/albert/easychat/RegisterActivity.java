package ru.albert.easychat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import javax.websocket.DeploymentException;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        Button submitRegisterButton = findViewById(R.id.submitRegisterButton);
        final EditText userNameText = findViewById(R.id.userName);
        final EditText passwordText = findViewById(R.id.password);
        submitRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                class ServerConnector extends AsyncTask<Void, Void, Void> {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            StartActivity.session.getBasicRemote().sendText(new Message(passwordText.getText().toString(), userNameText.getText().toString(), "register").toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                    }
                }
                ServerConnector connector = new ServerConnector();
                connector.execute();
            }
        });
    }
}