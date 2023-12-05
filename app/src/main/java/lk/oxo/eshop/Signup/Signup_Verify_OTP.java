package lk.oxo.eshop.Signup;

import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import lk.oxo.eshop.R;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.auth.AuthHandler;
import lk.oxo.eshop.util.auth.AuthenticationManager;
import lk.oxo.eshop.util.auth.PhoneAuthCallback;
import lk.oxo.eshop.util.Reciever;
import lk.oxo.eshop.util.SmsListener;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;

public class Signup_Verify_OTP extends Fragment implements SmsListener, PhoneAuthCallback, ProgressBarInterface {
    private static final long COUNTDOWN_DURATION = 30 * 1000;
    private static final long INTERVAL = 1000;
    private TextView resendText;
    private TextView otpError;
    private EditText otp;
    private Button resend_btn, continue_btn;
    private AuthenticationManager authenticationManager;
    private ProgressBar progressBar;
    private Reciever reciever;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup__verify__o_t_p, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(reciever);
    }

    @Override
    public void onResume() {
        super.onResume();
        reciever = new Reciever(this);
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        requireActivity().registerReceiver(reciever, intentFilter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        continue_btn = view.findViewById(R.id.button19);
        otp = view.findViewById(R.id.pinView);

        resendText = view.findViewById(R.id.textView22);
        TextView mobileText = view.findViewById(R.id.textView21);
        resend_btn = view.findViewById(R.id.button20);

        progressBar = view.findViewById(R.id.progressBar2);
        otpError = view.findViewById(R.id.textView10);

        Bundle data = getArguments();

        if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO) {
            continue_btn.setBackgroundResource(R.drawable.button_background_continue_night_disable);
            resend_btn.setBackgroundResource(R.drawable.button_background_continue_night_disable);
        }

        if (data != null) {
            String mobile = data.getString(getString(R.string.mobile_bundle));
            authenticationManager = AuthenticationManager.getInstance();
            mobileText.setText(getString(R.string.security_desc) + " " + generateMaskedMobile(mobile));
        }

        TextWatcher watcher = new TextWatcher() {
            String otp_;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                otp_ = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String otp1 = otp.getText().toString();

                if (!otp1.isEmpty() && Validation.validateOtp(otp1)) {
                    continue_btn.setEnabled(true);
                    if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_YES)
                        continue_btn.setBackgroundResource(R.drawable.button_background_continue_night);

                    if (!otp_.equals(s.toString()))
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

        otp.addTextChangedListener(watcher);
        startCountDownTimer();

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                callAuth(otp.getText().toString());
            }
        });

        resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationManager.resendOtp(getActivity());
            }
        });
    }

    private void callAuth(String otp) {
        authenticationManager.callAuth(this, otp, getContext());
    }

    private void startCountDownTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(COUNTDOWN_DURATION, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String text = getString(R.string.resend_code_in) + " " + seconds + " " + getString(R.string.seconds);
                resendText.setText(text);
            }

            @Override
            public void onFinish() {
                resend_btn.setEnabled(true);
                if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO) {
                    resend_btn.setBackgroundResource(R.drawable.button_background_border_white);
                    resend_btn.setTextColor(getResources().getColor(R.color.white));
                } else {
                    resend_btn.setBackgroundResource(R.drawable.button_background_border_black);
                    resend_btn.setTextColor(getResources().getColor(R.color.black));
                }
                resendText.setText("");
            }
        }.start();
    }

    private String generateMaskedMobile(String mobile) {
        String lastdigits = mobile.substring(mobile.length() - 2);

        StringBuilder builder = new StringBuilder();
        builder.append(mobile.charAt(1));

        for (int i = 2; i < mobile.length() - 2; i++) {
            builder.append(getString(R.string.x));
            if (i == 2 || i == 5) {
                builder.append(" ");
            }
        }

        builder.append(lastdigits);
        return builder.toString();
    }

    @Override
    public void onSmsReceived(String sms) {
        otp.setText(sms);
    }

    @Override
    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            otp.setEnabled(false);
        }
    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            otp.setEnabled(true);
        }
    }

    @Override
    public void showErrorMessage() {
        otpError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorMessage() {
        otpError.setVisibility(View.GONE);
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
        showErrorMessage();
    }
}