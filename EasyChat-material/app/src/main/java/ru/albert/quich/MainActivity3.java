package ru.albert.quich;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.spec.KeySpec;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class MainActivity3 extends AppCompatActivity {
    public static EditText messageText;
    public static Context context;
    public static TextView messagesText;
    public static AppCompatActivity activity;
    public static String salt;
    public static String pass;
    public static Cipher cipher;
    public static IvParameterSpec parameterSpec;
    public static ProgressDialog dialog;
    public static SecretKey sKey;
    public static SecretKey getKeyFromPassword(String password, String salt) throws Exception {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 10, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;
    }
    public static String encrypt(String input) throws Exception{
        cipher = Cipher.getInstance("AES/GCM/NoPadding");
        parameterSpec = new IvParameterSpec(new byte[cipher.getBlockSize()]);
        cipher.init(Cipher.ENCRYPT_MODE, sKey, parameterSpec);
        byte[] encryptedByte = cipher.doFinal(input.getBytes());
        return Base64.encodeToString(encryptedByte, 11);
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.mainactivity2);
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getString(R.string.findingChat));
            dialog.setCancelable(false);
            //StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            //StrictMode.setThreadPolicy(gfgPolicy);
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
                                    String encryptedText = encrypt(messageText.getText().toString());
                                    TurboSession.sendMessage(new Message(ClientEndpoint.chatID, "send", 0, encryptedText, System.currentTimeMillis(), false));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            messageText.setText("");
                                        }
                                    });
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
                                TurboSession.sendMessage(new Message("newChat", "dummy"));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messageText.setText("");
                                    }
                                });
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
                    NewChat downloader1 = new NewChat();
                    downloader1.execute();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}