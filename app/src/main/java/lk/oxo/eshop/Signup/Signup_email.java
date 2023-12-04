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

import lk.oxo.eshop.Login.Signin_main;
import lk.oxo.eshop.R;
import lk.oxo.eshop.model.User;
import lk.oxo.eshop.util.ButtonColor;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;

public class Signup_email extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button signin = view.findViewById(R.id.button13);
        Button continueBtn = view.findViewById(R.id.button11);

        ButtonColor buttonColor = new ButtonColor(0, 18, 25);

        if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_NO)
            signin.setText(buttonColor.
                    changeButtonText(getString(R.string.already_member),
                            getContext(), getString(R.string.day)));
        else {
            continueBtn.setBackgroundResource(R.drawable.button_background_continue_night_disable);
            signin.setText(buttonColor.
                    changeButtonText(getString(R.string.already_member),
                            getContext(), getString(R.string.night)));
        }

        EditText email = view.findViewById(R.id.email_signup);
        EditText fname = view.findViewById(R.id.fname_signup);
        EditText lname = view.findViewById(R.id.lname_signup);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email1 = email.getText().toString().trim();
                String fname1 = fname.getText().toString().trim();
                String lname1 = lname.getText().toString().trim();

                if (!email1.isEmpty() && !fname1.isEmpty() && !lname1.isEmpty() && Validation.checkEmail(email1)) {
                    continueBtn.setEnabled(true);
                    if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_YES)
                        continueBtn.setBackgroundResource(R.drawable.button_background_continue_night);
                } else {
                    continueBtn.setEnabled(false);
                    if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_YES)
                        continueBtn.setBackgroundResource(R.drawable.button_background_continue_night_disable);
                    else
                        continueBtn.setBackgroundResource(R.drawable.disable_button);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        email.addTextChangedListener(watcher);
        fname.addTextChangedListener(watcher);
        lname.addTextChangedListener(watcher);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString(getString(R.string.email_bundle), email.getText().toString());
                data.putString(getString(R.string.fname_bundle), fname.getText().toString());
                data.putString(getString(R.string.lname_bundle), lname.getText().toString());

                Signup_Enter_Password enter_password = new Signup_Enter_Password();
                enter_password.setArguments(data);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, enter_password, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, Signin_main.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}