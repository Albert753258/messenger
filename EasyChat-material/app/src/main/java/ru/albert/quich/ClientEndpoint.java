package ru.albert.quich;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;

@javax.websocket.ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ClientEndpoint {
    public static long chatID = 0;
    public static String decrypt(String input) throws Exception{
        MainActivity3.cipher = Cipher.getInstance("AES/GCM/NoPadding");
        MainActivity3.parameterSpec = new IvParameterSpec(new byte[MainActivity3.cipher.getBlockSize()]);
        MainActivity3.cipher.init(Cipher.DECRYPT_MODE, MainActivity3.sKey, MainActivity3.parameterSpec);
        byte[] decryptedByte = MainActivity3.cipher.doFinal(Base64.decode(input, 11));
        return new String(decryptedByte);
    }

    @OnMessage
    public void onMessage(final Message message) throws Exception {
        if(message.action.equals("loginhash")){
            String loginHash = message.text;
            StartActivity.loginEditor.putString("loginhash", loginHash);
            StartActivity.userName = message.userName;
            StartActivity.sessionHash = loginHash;
            StartActivity.loginEditor.putString("username", message.userName);
            StartActivity.loginEditor.putInt("verified", 0);
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
            Thread.sleep(1500);
            MainActivity3.salt = message.text;
            MainActivity3.pass = message.passHash;
            chatID = message.id;
            MainActivity3.dialog.cancel();
            MainActivity3.sKey = MainActivity3.getKeyFromPassword(message.passHash, message.text);
        }
        else if(message.action.equals("logininvalid")){
            LoginActivity.loginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoginFragment.passwordTextInput.setError(LoginActivity.loginActivity.getString(R.string.loginIncorrect));
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
        else if(message.action.equals("confirmEmail")){
            Intent intent = new Intent(StartActivity.startActivity, EmailConfirmActivity.class);
            StartActivity.startActivity.startActivity(intent);
        }
        else if(message.action.equals("emailConfirmInvalid")){
            //todo strings
            EmailConfirmFragment.materialTextInput.setError("Invalid confirmation code");
        }
        else if(message.action.equals("emailexist")){
            RegisterActivity.registerActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RegisterFragment.register_email_text_input.setError(RegisterActivity.registerActivity.getString(R.string.emailExist));
                }
            });
        }
        else if(message.action.equals("emailConfirmOk")){
            class SessionChecker extends AsyncTask<Void, Void, Void> {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        StartActivity.loginEditor.putInt("verified", 0);
                        StartActivity.loginEditor.apply();
                        StartActivity.verified = 0;
                        TurboSession.sendMessage(new Message("sessionHashCheck", StartActivity.sessionHash));
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
        else {
            MainActivity3.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String decoded = "";
                    try {
                        decoded = decrypt(message.text);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MainActivity3.messagesText.append(decoded + "\n");
                }
            });
        }
    }
    @OnClose
    public void onClose(){
        MainActivity3.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //todo strings
                Toast.makeText(MainActivity3.activity.getApplicationContext(), "Session closed", Toast.LENGTH_LONG).show();
            }
        });
    }
}