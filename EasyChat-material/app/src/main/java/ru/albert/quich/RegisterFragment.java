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


public class RegisterFragment extends Fragment {

    public static TextInputLayout passwordTextInput;
    public static TextInputEditText materialUsername;
    public static TextInputEditText passwordEditText;
    public static TextInputLayout register_email_text_input;
    public static TextInputLayout registerUserInput;
    public static MaterialButton nextButton;
    public static TextInputEditText emailEditText;
    public static MaterialButton cancelButton;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        register_email_text_input = view.findViewById(R.id.register_email_text_input);
        passwordTextInput = view.findViewById(R.id.register_password_text_input);
        registerUserInput = view.findViewById(R.id.registerUserInput);
        emailEditText = view.findViewById(R.id.register_email_edit_text);
        passwordEditText = view.findViewById(R.id.register_password_edit_text);
        materialUsername = view.findViewById(R.id.register_materialUsername);
        nextButton = view.findViewById(R.id.register_next_button);
        cancelButton = view.findViewById(R.id.register_cancel_button);
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                }
                return false;
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(getString(R.string.shortPasswordError));
                } else {
                    class RegisterSender extends AsyncTask<Void, Void, Void> {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                StartActivity.session.getBasicRemote().sendText(new Message(passwordEditText.getText().toString(), materialUsername.getText().toString(), "register", emailEditText.getText().toString()).toString());
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
                    RegisterSender connector = new RegisterSender();
                    connector.execute();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.registerActivity.finish();
            }
        });
        return view;
    }

    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }

    // "isPasswordValid" from "Navigate to the next Fragment" section method goes here
}

