package lk.oxo.eshop.Signup;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import lk.oxo.eshop.Login.Signin_main;
import lk.oxo.eshop.R;
import lk.oxo.eshop.util.ButtonColor;
import lk.oxo.eshop.util.Validation;

public class SignupMobile extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_mobile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button signin = view.findViewById(R.id.button17);
        ButtonColor buttonColor = new ButtonColor(0, 18, 25);
        signin.setText(buttonColor.changeButtonText("Already a member? Sign in", getContext()));

        EditText mobile = view.findViewById(R.id.mobile_signup1);
        EditText fname = view.findViewById(R.id.fname_signup1);
        EditText lname = view.findViewById(R.id.lname_signup1);

        Button continueBtn = view.findViewById(R.id.button15);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobile1 = mobile.getText().toString().trim();
                String fname1 = fname.getText().toString().trim();
                String lname1 = lname.getText().toString().trim();

                continueBtn.setEnabled(!mobile1.isEmpty() && !fname1.isEmpty() && !lname1.isEmpty()  && Validation.checkMobile(mobile1));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mobile.addTextChangedListener(watcher);
        fname.addTextChangedListener(watcher);
        lname.addTextChangedListener(watcher);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, Signup_Enter_Password.class,null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, Signin_main.class,null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}