package lk.oxo.eshop.Signup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import lk.oxo.eshop.R;

public class Signup_Enter_Password extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup__enter__password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText password = view.findViewById(R.id.password_signup);
        TextView password_hint = view.findViewById(R.id.textView15);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_animation);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = password.getText().toString().trim();
                String message = getMessage(pass);

                if(!password_hint.getText().toString().equals(message)){
                    password_hint.setText(message);
                    password_hint.startAnimation(animation);
                }

                if(message.isEmpty())
                    view.findViewById(R.id.button14).setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        password.addTextChangedListener(watcher);

    }

    private String getMessage(String password) {
        if (password.isEmpty()) {
            return getString(R.string.password_hint_empty);
        } else if (password.length() < 8) {
            if (!password.matches(".*[a-zA-Z].*")) {
                return getString(R.string.password_hint_1);
            } else if (!password.matches(".*[0-9\\W_].*")) {
                return getString(R.string.password_hint_2);
            } else {
                return getString(R.string.password_hint_3);
            }
        } else if (!password.matches(".*[a-zA-Z].*")) {
            if (!password.matches(".*[0-9\\W_].*")) {
                return getString(R.string.password_hint_4);
            } else {
                return getString(R.string.password_hint_5);
            }
        } else if (!password.matches(".*[0-9\\W_].*")) {
            return getString(R.string.password_hint_6);
        } else {
            return "";
        }
    }
}