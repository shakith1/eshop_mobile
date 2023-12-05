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

import lk.oxo.eshop.R;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;

public class LinkAccount extends Fragment {
    private EditText email,password;
    private Button link,reset;
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

        if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO){
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

                if(!email1.isEmpty() && !password1.isEmpty() && Validation.checkEmail(email1)){
                    link.setEnabled(true);
                    link.setBackgroundResource(R.drawable.button_background_continue_night);
                }else{
                    link.setBackgroundResource(R.drawable.button_background_continue_night_disable);
                }
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

        if(!email_.isEmpty())
            email.setText(email_);
    }
}