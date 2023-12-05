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
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.User;
import lk.oxo.eshop.util.auth.AuthHandler;
import lk.oxo.eshop.util.auth.AuthenticationManager;
import lk.oxo.eshop.util.auth.PhoneAuthCallback;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;

public class Signup_Enter_Mobile extends Fragment implements PhoneAuthCallback {
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private AuthenticationManager authenticationManager;
    private User user;
    private FirebaseAuth firebaseAuth;
    private Bundle user_bundle;
    private String mobile_text;
    private ProgressBar progressBar;
    private EditText mobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup__enter__mobile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        Button continue_btn = view.findViewById(R.id.button18);
        mobile = view.findViewById(R.id.mobile_signup1);

        progressBar = view.findViewById(R.id.progressBar);

//        UI Customization

        if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO)
            continue_btn.setBackgroundResource(R.drawable.button_background_continue_night_disable);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobile1 = mobile.getText().toString();

                if (!mobile1.isEmpty() && Validation.checkMobile(mobile1)) {
                    continue_btn.setEnabled(true);
                    if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_YES)
                        continue_btn.setBackgroundResource(R.drawable.button_background_continue_night);
                } else {
                    if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_YES)
                        continue_btn.setBackgroundResource(R.drawable.button_background_continue_night_disable);
                    else
                        continue_btn.setBackgroundResource(R.drawable.disable_button);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mobile.addTextChangedListener(watcher);

//        End UI Customization

        Bundle data = getArguments();

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                mobile_text = mobile.getText().toString();

                if (data != null) {
                    user_bundle = data.getBundle(getString(R.string.user_bundle));

                    user = new User();
                    user.setEmail(user_bundle.getString(getString(R.string.email_bundle)));
                    user.setFname(user_bundle.getString(getString(R.string.fname_bundle)));
                    user.setLname(user_bundle.getString(getString(R.string.lname_bundle)));
                    user.setPassword(data.getString(getString(R.string.password_bundle)));
                    user.setMobile(mobile_text);

                    authenticationManager = new AuthenticationManager(user);
                    authenticationManager.createAccount(mobile_text, getActivity(), callbacks);
                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                callAuth(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getContext(), getString(R.string.user_creation_error) + " " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideProgressBar();
            }

            @Override
            public void onCodeSent(@NonNull String verifcationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verifcationId, forceResendingToken);
                authenticationManager.setVerificationId(verifcationId);
                authenticationManager.setResendingToken(forceResendingToken);
                Toast.makeText(getContext(), getString(R.string.verify_otp), Toast.LENGTH_SHORT).show();
                callOtpFragment();
            }
        };
    }

    public void callAuth(PhoneAuthCredential phoneAuthCredential){
        authenticationManager.createAccountAuth(phoneAuthCredential,this,getContext());
    }

    public void callOtpFragment() {
        Bundle data_second = new Bundle();
        data_second.putBundle(getString(R.string.user_bundle), user_bundle);
        data_second.putString(getString(R.string.mobile_bundle), mobile_text);
        data_second.putSerializable(getString(R.string.authentication), authenticationManager);

        Signup_Verify_OTP verify_otp = new Signup_Verify_OTP();
        verify_otp.setArguments(data_second);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, verify_otp, null)
                .addToBackStack(null)
                .commit();
    }

    private void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            mobile.setEnabled(false);
        }
    }

    private void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            mobile.setEnabled(true);
        }
    }

    @Override
    public void authSuccess() {
        hideProgressBar();
        AuthHandler.handleSuccess();
    }

    @Override
    public void authFailed(String message) {
        hideProgressBar();
        AuthHandler.handleFailed(getContext(),message);
    }
}