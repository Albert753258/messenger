package ru.albert.quich;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public class LoginFragment extends Fragment {

    public static TextInputLayout passwordTextInput;
    public static TextInputEditText materialUsername;
    public static TextInputLayout loginMaterialInput;
    public static TextInputEditText passwordEditText;
    public static MaterialButton nextButton;
    public static MaterialButton cancelButton;
    public static View view;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        passwordTextInput = view.findViewById(R.id.login_password_text_input);
        passwordEditText = view.findViewById(R.id.login_password_edit_text);
        loginMaterialInput = view.findViewById(R.id.loginMaterialInput);
        materialUsername = view.findViewById(R.id.login_materialUsername);
        nextButton = view.findViewById(R.id.login_next_button);
        cancelButton = view.findViewById(R.id.login_cancel_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                class LoginSender extends AsyncTask<Void, Void, Void> {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            StartActivity.session.getBasicRemote().sendText(new Message(passwordEditText.getText().toString(), materialUsername.getText().toString(), "login").toString());
                        } catch (IOException | NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                    }
                }
                LoginSender connector = new LoginSender();
                connector.execute();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.loginActivity.finish();
            }
        });
        return view;
    }

    // "isPasswordValid" from "Navigate to the next Fragment" section method goes here
}

