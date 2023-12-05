package lk.oxo.eshop.Signup;

import android.content.res.ColorStateList;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import lk.oxo.eshop.Login.Signin_main;
import lk.oxo.eshop.R;
import lk.oxo.eshop.model.User;
import lk.oxo.eshop.util.ButtonColor;
import lk.oxo.eshop.util.FirebaseValidationCallback;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;

public class Signup_email extends Fragment implements ProgressBarInterface {
    private FirebaseFirestore firestore;
    private TextInputLayout emailField;
    private ProgressBar progressBar;
    private EditText email, fname, lname;
    private Button signin, continueBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        signin = view.findViewById(R.id.button13);
        continueBtn = view.findViewById(R.id.button11);
        emailField = view.findViewById(R.id.textInput_layout_1);
        progressBar = view.findViewById(R.id.progressBar3);

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

        email = view.findViewById(R.id.email_signup);
        fname = view.findViewById(R.id.fname_signup);
        lname = view.findViewById(R.id.lname_signup);

        TextWatcher watcher = new TextWatcher() {
            String emailEnter;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailEnter = s.toString();
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

                    if (!emailEnter.equals(s.toString()))
                        hideErrorMessage();
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
                showProgressBar();
                checkEmail(email.getText().toString(), new FirebaseValidationCallback() {
                    @Override
                    public void onResult(boolean exists) {
                        if(exists){
                            hideProgressBar();
                            showErrorMessage();
                        }else{
                            Bundle data = new Bundle();
                            data.putString(getString(R.string.email_bundle), email.getText().toString());
                            data.putString(getString(R.string.fname_bundle), fname.getText().toString());
                            data.putString(getString(R.string.lname_bundle), lname.getText().toString());

                            Signup_Enter_Password enter_password = new Signup_Enter_Password();
                            enter_password.setArguments(data);

                            hideProgressBar();

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragmentContainerView, enter_password, null)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });
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

    @Override
    public void showErrorMessage() {
        emailField.setErrorEnabled(true);
        emailField.setError(getString(R.string.email_error));
        emailField.setBoxStrokeErrorColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        emailField.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
    }

    @Override
    public void hideErrorMessage() {
        emailField.setErrorEnabled(false);
    }

    private void checkEmail(String email, FirebaseValidationCallback callback) {
        CollectionReference users = firestore.collection(getString(R.string.users));
        Query query = users.whereEqualTo(getString(R.string.email_collection), email);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            boolean exists = result != null && !result.isEmpty();
                            callback.onResult(exists);
                        } else
                            callback.onResult(false);
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
}