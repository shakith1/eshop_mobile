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

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.FirebaseUser;
import lk.oxo.eshop.util.ButtonColor;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;
import lk.oxo.eshop.util.auth.google.GoogleAuthentication;

public class Create_Account_Google extends Fragment implements ProgressBarInterface {
    private EditText email, fname, lname;
    private Button link, create;
    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;
    private GoogleAuthentication googleAuthentication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create__account__google, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        UI Customization

        email = view.findViewById(R.id.email_signup_google);
        fname = view.findViewById(R.id.fname_signup_google);
        lname = view.findViewById(R.id.lname_signup_google);
        link = view.findViewById(R.id.button16);
        create = view.findViewById(R.id.button17);

        progressBar = view.findViewById(R.id.progressBar4);

        ButtonColor buttonColor = new ButtonColor(0, 17, 41);

        if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_NO)
            link.setText(buttonColor.
                    changeButtonText(getString(R.string.already_eshop),
                            getContext(), getString(R.string.day)));
        else {
            create.setBackgroundResource(R.drawable.button_background_continue_night_disable);
            link.setText(buttonColor.
                    changeButtonText(getString(R.string.already_eshop),
                            getContext(), getString(R.string.night)));
        }

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
                    create.setEnabled(true);
                    if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_YES)
                        create.setBackgroundResource(R.drawable.button_background_continue_night);
                } else {
                    create.setEnabled(false);
                    if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_YES)
                        create.setBackgroundResource(R.drawable.button_background_continue_night_disable);
                    else
                        create.setBackgroundResource(R.drawable.disable_button);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        email.addTextChangedListener(watcher);
        fname.addTextChangedListener(watcher);
        lname.addTextChangedListener(watcher);

//        End UI Customization

        Bundle bundle = getArguments();
        String uid_ = bundle.getString(getString(R.string.uid_bundle));
        String email_ = bundle.getString(getString(R.string.email_bundle));
        String fname_ = bundle.getString(getString(R.string.fname_bundle));
        String lname_ = bundle.getString(getString(R.string.lname_bundle));

        email.setText(email_);
        fname.setText(fname_);
        lname.setText(lname_);

        googleAuthentication = new GoogleAuthentication(this,getContext());

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser = new FirebaseUser(uid_,email.getText().toString(),
                        fname.getText().toString(),lname.getText().toString());
                googleAuthentication.createAccount(firebaseUser);
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, new LinkAccount(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        email.setEnabled(false);
        fname.setEnabled(false);
        lname.setEnabled(false);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        email.setEnabled(true);
        fname.setEnabled(true);
        lname.setEnabled(true);
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void hideErrorMessage() {

    }
}