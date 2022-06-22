package ru.albert.easychat;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.websocket.OnMessage;

@javax.websocket.ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ClientEndpoint {

    @OnMessage
    public void onMessage(final Message message) {
        //final TextView textView = new TextView(MainActivity3.context);
        if(message.action.equals("loginhash")){
            String loginHash = message.text;
            StartActivity.loginEditor.putString("loginhash", loginHash);
            StartActivity.userName = message.userName;
            StartActivity.sessionHash = loginHash;
            StartActivity.loginEditor.putString("username", message.userName);
            StartActivity.loginEditor.apply();
            Intent intent = new Intent(StartActivity.startActivity, MainActivity3.class);
            StartActivity.startActivity.startActivity(intent);
        }
        else if(message.action.equals("logininvalid")){
            Toast.makeText(LoginActivity.loginActivity.getApplicationContext(), "LoginActivity.loginActivity.getString(R.string.loginIncorrect)", Toast.LENGTH_LONG).show();
        }
        else {
            MainActivity3.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity3.messagesText.append(message.text + "\n");
                }
            });
            //textView.setText(message.text);
        }
        /*
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        MainActivity3.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity3.tl.addView(textView);
                MainActivity3.scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity3.scrollView.scrollTo(0, textView.getHeight());
                        MainActivity3.scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });

            }
        });
        */
    }
}