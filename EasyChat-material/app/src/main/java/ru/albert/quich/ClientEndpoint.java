package ru.albert.quich;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import javax.websocket.OnMessage;

@javax.websocket.ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ClientEndpoint {
    public static long chatID = 0;

    @OnMessage
    public void onMessage(final Message message) throws InterruptedException {
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
            StartActivity.startActivity.finish();
            if(LoginActivity.loginActivity != null){
                LoginActivity.loginActivity.finish();
            }
            if(RegisterActivity.registerActivity != null){
                RegisterActivity.registerActivity.finish();
            }
        }
        else if(message.action.equals("chatFound")){
            MainActivity3.dialog.cancel();
            chatID = message.id;
        }
        else if(message.action.equals("logininvalid")){
            LoginActivity.loginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoginFragment.passwordTextInput.setError(LoginActivity.loginActivity.getString(R.string.loginIncorrect));
                    //Toast.makeText(LoginActivity.loginActivity.getApplicationContext(), LoginActivity.loginActivity.getString(R.string.loginIncorrect), Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(message.action.equals("sessionInvalid")){
            Intent intent = new Intent(StartActivity.startActivity, LoginActivity.class);
            StartActivity.startActivity.startActivity(intent);
            StartActivity.startActivity.finish();
            Thread.sleep(1000);
            LoginActivity.loginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.loginActivity.getApplicationContext(), LoginActivity.loginActivity.getString(R.string.loginAgain), Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(message.action.equals("sessionValid")){
            Intent intent = new Intent(StartActivity.startActivity, MainActivity3.class);
            StartActivity.startActivity.startActivity(intent);
            StartActivity.startActivity.finish();
        }
        else if(message.action.equals("usernameexist")){
            RegisterActivity.registerActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RegisterFragment.registerUserInput.setError(RegisterActivity.registerActivity.getString(R.string.usernameExist));
                }
            });
        }
        else if(message.action.equals("emailexist")){
            RegisterActivity.registerActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RegisterFragment.register_email_text_input.setError(RegisterActivity.registerActivity.getString(R.string.emailExist));
                }
            });
        }
        else {
            MainActivity3.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity3.messagesText.append(message.text + "\n");
                }
            });
        }
    }
}