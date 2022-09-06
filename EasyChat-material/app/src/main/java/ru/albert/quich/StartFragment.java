package ru.albert.quich;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;


public class StartFragment extends Fragment {

    public static MaterialButton loginButton;
    public static MaterialButton registerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_fragment, container, false);
        loginButton = view.findViewById(R.id.startLoginButton);
        registerButton = view.findViewById(R.id.startRegisterButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.startActivity, LoginActivity.class);
                startActivity(intent);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.startActivity, RegisterActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}

