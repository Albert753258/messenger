package ru.albert.quich;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EmailConfirmActivity extends AppCompatActivity {
    public static AppCompatActivity emailConfirmActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_container);
        emailConfirmActivity = this;
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new EmailConfirmFragment())
                    .commit();
        }
    }
}