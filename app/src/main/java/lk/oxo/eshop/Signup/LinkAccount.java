package lk.oxo.eshop.Signup;

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

import lk.oxo.eshop.R;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;
import lk.oxo.eshop.util.auth.EmailSignin;

public class LinkAccount extends Fragment implements ProgressBarInterface {
    private EditText email, password;
    private ProgressBar progressBar;
    private Button link, reset;
    private TextView error;
    private LinkAccount linkAccount = this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_link_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.email_signin_google);
        password = view.findViewById(R.id.password_signin_google);
        link = view.findViewById(R.id.button10);
        reset = view.findViewById(R.id.button15);
        error = view.findViewById(R.id.textView29);
        progressBar = view.findViewById(R.id.progressBar8);

        if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO) {
            link.setBackgroundResource(R.drawable.button_background_continue_night_disable);
        }

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email1 = email.getText().toString().trim();
                String password1 = password.getText().toString().trim();

                if (!email1.isEmpty() && !password1.isEmpty() && Validation.checkEmail(email1)) {
                    link.setEnabled(true);
                    link.setBackgroundResource(R.drawable.button_background_continue_night);
                } else {
                    link.setBackgroundResource(R.drawable.button_background_continue_night_disable);
                }

                hideErrorMessage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        email.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);

//        End UI Customization

        Bundle data = getArguments();
        String email_ = data.getString(getString(R.string.email_bundle));

        if (!email_.isEmpty())
            email.setText(email_);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailSignin emailSignin = new EmailSignin(linkAccount, getContext(),
                        email.getText().toString(), password.getText().toString(),false);
                emailSignin.signinUserEmail();
            }
        });
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        email.setEnabled(false);
        password.setEnabled(false);
        link.setEnabled(false);
        reset.setEnabled(false);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        email.setEnabled(true);
        password.setEnabled(true);
        link.setEnabled(true);
        reset.setEnabled(true);
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