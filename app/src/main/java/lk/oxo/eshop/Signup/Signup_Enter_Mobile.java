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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.User;
import lk.oxo.eshop.util.auth.google.FirebaseValidationCallback;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.auth.AuthHandler;
import lk.oxo.eshop.util.auth.AuthenticationManager;
import lk.oxo.eshop.util.auth.PhoneAuthCallback;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;

public class Signup_Enter_Mobile extends Fragment implements PhoneAuthCallback, ProgressBarInterface {
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private AuthenticationManager authenticationManager;
    private User user;
    private Bundle user_bundle;
    private String mobile_text;
    private ProgressBar progressBar;
    private EditText mobile;
    private FirebaseFirestore firestore;
    private TextInputLayout mobileField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup__enter__mobile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        Button continue_btn = view.findViewById(R.id.button18);
        mobile = view.findViewById(R.id.mobile_signup1);

        progressBar = view.findViewById(R.id.progressBar);
        mobileField = view.findViewById(R.id.textInput_layout_2);

//        UI Customization

        if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO)
            continue_btn.setBackgroundResource(R.drawable.button_background_continue_night_disable);

        TextWatcher watcher = new TextWatcher() {
            String mobile_;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mobile_ = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobile1 = mobile.getText().toString();

                if (!mobile1.isEmpty() && Validation.checkMobile(mobile1)) {
                    continue_btn.setEnabled(true);
                    if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_YES)
                        continue_btn.setBackgroundResource(R.drawable.button_background_continue_night);

                    if (!mobile_.equals(s.toString()))
                        hideErrorMessage();
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

                checkMobile(mobile_text, new FirebaseValidationCallback() {
                    @Override
                    public void onResult(boolean exists,String authentication) {
                        if(exists){
                            hideProgressBar();
                            showErrorMessage();
                        }else{
                            if (data != null) {
                                user_bundle = data.getBundle(getString(R.string.user_bundle));

                                user = new User();
                                user.setEmail(user_bundle.getString(getString(R.string.email_bundle)));
                                user.setFname(user_bundle.getString(getString(R.string.fname_bundle)));
                                user.setLname(user_bundle.getString(getString(R.string.lname_bundle)));
                                user.setPassword(data.getString(getString(R.string.password_bundle)));
                                user.setMobile(mobile_text);

                                authenticationManager = AuthenticationManager.getInstance(user);
                                authenticationManager.createAccount(mobile_text, getActivity(), callbacks);

                            }
                        }
                    }
                });
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

    public void callAuth(PhoneAuthCredential phoneAuthCredential) {
        authenticationManager.createAccountAuth(phoneAuthCredential, this, getContext());
    }

    public void callOtpFragment() {
        Bundle data_second = new Bundle();
        data_second.putBundle(getString(R.string.user_bundle), user_bundle);
        data_second.putString(getString(R.string.mobile_bundle), mobile_text);

        Signup_Verify_OTP verify_otp = new Signup_Verify_OTP();
        verify_otp.setArguments(data_second);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, verify_otp, null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            mobile.setEnabled(false);
        }
    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            mobile.setEnabled(true);
        }
    }

    @Override
    public void showErrorMessage() {
        mobileField.setErrorEnabled(true);
        mobileField.setError(getString(R.string.mobile_error));
        mobileField.setBoxStrokeErrorColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        mobileField.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
    }

    @Override
    public void hideErrorMessage() {
        mobileField.setErrorEnabled(false);
    }

    @Override
    public void authSuccess() {
        hideProgressBar();
        AuthHandler.handleSuccess(getContext());
    }

    @Override
    public void authFailed(String message) {
        hideProgressBar();
        AuthHandler.handleFailed(getContext(), message);
    }

    @Override
    public void authFailed() {

    }

    private void checkMobile(String mobile, FirebaseValidationCallback callback) {
        CollectionReference users = firestore.collection(getString(R.string.users));
        Query query = users.whereEqualTo(getString(R.string.mobile_collection), mobile);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            boolean exists = result != null && !result.isEmpty();
                            callback.onResult(exists,null);
                        } else
                            callback.onResult(false,null);
                    }
                });
    }
}