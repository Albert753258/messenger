package ru.albert.easychat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;

import javax.websocket.OnMessage;
import java.text.SimpleDateFormat;

@javax.websocket.ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ClientEndpoint {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    @OnMessage
    public void onMessage(final Message message) {
//        TextView textView = new TextView(MainActivity3.context);
//        textView.setText(message.text);
//        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
//        textView.setLayoutParams(params);
//        MainActivity3.tl.addView(textView);
        //MainActivity2.chatText.append(message.text + "\n");
        //MainActivity2.scrollView.fullScroll(View.FOCUS_DOWN);
        final TextView textView = new TextView(MainActivity3.context);
        textView.setText(message.text);
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
    }
}