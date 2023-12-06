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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import lk.oxo.eshop.R;
import lk.oxo.eshop.Signup.Create_Account;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;
import lk.oxo.eshop.util.auth.EmailSignin;

public class Signin extends Fragment implements ProgressBarInterface {
    private ProgressBar progressBar;
    private EditText email,password;
    private Button signin,reset,create;
    private TextView error;
    private Signin sign = this;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         email= view.findViewById(R.id.email_signin);
         password = view.findViewById(R.id.password_signin);
         signin = view.findViewById(R.id.button3);
         reset = view.findViewById(R.id.button4);
         create = view.findViewById(R.id.button6);

         error = view.findViewById(R.id.textView28);

         progressBar = view.findViewById(R.id.progressBar7);

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

                if(!email1.isEmpty() && !password1.isEmpty() &&
                        (Validation.checkEmail(email1) || Validation.checkMobile(email1))){
                    signin.setEnabled(true);
                    signin.setBackgroundResource(R.drawable.button_background_continue_night);
                }else{
                    signin.setBackgroundResource(R.drawable.button_background_continue_night_disable);
                }

                hideErrorMessage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        email.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, Create_Account.class,null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailSignin emailSignin = new EmailSignin(sign, getContext(),
                        email.getText().toString(),password.getText().toString());
                emailSignin.chooseSignInMethod();
            }
        });
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        email.setEnabled(false);
        password.setEnabled(false);
        signin.setEnabled(false);
        reset.setEnabled(false);
        create.setEnabled(false);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        email.setEnabled(true);
        password.setEnabled(true);
        signin.setEnabled(true);
        reset.setEnabled(true);
        create.setEnabled(true);
    }

    @Override
    public void showErrorMessage() {
        error.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorMessage() {
        error.setVisibility(View.GONE);
    }
}