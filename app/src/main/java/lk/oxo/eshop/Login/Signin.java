package lk.oxo.eshop.Login;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import lk.oxo.eshop.R;
import lk.oxo.eshop.Signup.Create_Account;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;

public class Signin extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText email = view.findViewById(R.id.email_signin);
        EditText password = view.findViewById(R.id.password_signin);
        Button signin = view.findViewById(R.id.button3);

        String email_, password_;

        if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO){
            signin.setBackgroundResource(R.drawable.button_background_continue_night_disable);
        }

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email1 = email.getText().toString().trim();
                String password1 = password.getText().toString().trim();

                if(!email1.isEmpty() && !password1.isEmpty() && Validation.checkEmail(email1)){
                    signin.setEnabled(true);
                    signin.setBackgroundResource(R.drawable.button_background_continue_night);
                }else{
                    signin.setBackgroundResource(R.drawable.button_background_continue_night_disable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        email.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);

        view.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, Create_Account.class,null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}