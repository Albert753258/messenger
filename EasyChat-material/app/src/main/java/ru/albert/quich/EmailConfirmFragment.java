package ru.albert.quich;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class EmailConfirmFragment extends Fragment {

    public static MaterialButton nextButton;
    public static MaterialButton cancelButton;
    public static TextInputLayout materialTextInput;
    public static TextInputEditText materialemailEdit;
    public static View view;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.emailconfirm_fragment, container, false);
        nextButton = view.findViewById(R.id.login_next_button);
        materialemailEdit = view.findViewById(R.id.emailMaterialEdit);
        materialTextInput = view.findViewById(R.id.emailMaterialInput);
        cancelButton = view.findViewById(R.id.login_cancel_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                class LoginSender extends AsyncTask<Void, Void, Void> {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Message message = new Message();
                            message.action="emailConfirmCheck";
                            message.authorId = Integer.parseInt(materialemailEdit.getText().toString());
                            message.sessionHash = StartActivity.sessionHash;
                            TurboSession.sendMessage(message);
                            //StartActivity.session.getBasicRemote().sendText(new Message(passwordEditText.getText().toString(), materialUsername.getText().toString(), "login").toString());
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
                LoginSender connector = new LoginSender();
                connector.execute();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailConfirmActivity.emailConfirmActivity.finish();
            }
        });
        return view;
    }

    // "isPasswordValid" from "Navigate to the next Fragment" section method goes here
}

