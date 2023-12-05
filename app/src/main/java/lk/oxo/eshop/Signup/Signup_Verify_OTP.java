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

import lk.oxo.eshop.R;
import lk.oxo.eshop.util.auth.AuthHandler;
import lk.oxo.eshop.util.auth.AuthenticationManager;
import lk.oxo.eshop.util.auth.PhoneAuthCallback;
import lk.oxo.eshop.util.Reciever;
import lk.oxo.eshop.util.SmsListener;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;

public class Signup_Verify_OTP extends Fragment implements SmsListener, PhoneAuthCallback {

    private static final long COUNTDOWN_DURATION = 30 * 1000;
    private static final long INTERVAL = 1000;
    private CountDownTimer countDownTimer;
    private TextView resendText, mobileText;
    private EditText otp;
    private Button reset_btn, continue_btn;
    private String mobile;
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
        mobileText = view.findViewById(R.id.textView21);
        reset_btn = view.findViewById(R.id.button20);

        progressBar = view.findViewById(R.id.progressBar2);

        Bundle data = getArguments();

        if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO) {
            continue_btn.setBackgroundResource(R.drawable.button_background_continue_night_disable);
            reset_btn.setBackgroundResource(R.drawable.button_background_continue_night_disable);
        }

        if (data != null) {
            mobile = data.getString(getString(R.string.mobile_bundle));
            authenticationManager = (AuthenticationManager) data.getSerializable(getString(R.string.authentication));

            mobileText.setText(getString(R.string.security_desc) + " " + generateMaskedMobile(mobile));
        }

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String otp1 = otp.getText().toString();

                if (!otp1.isEmpty() && Validation.validateOtp(otp1)) {
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

        otp.addTextChangedListener(watcher);
        startCountDownTimer();

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                callAuth(otp.getText().toString());
            }
        });

    }

    private void callAuth(String otp) {
        authenticationManager.callAuth(this, otp,getContext());
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(COUNTDOWN_DURATION, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String text = getString(R.string.resend_code_in) + " " + seconds + " " + getString(R.string.seconds);
                resendText.setText(text);
            }

            @Override
            public void onFinish() {
                reset_btn.setEnabled(true);
                if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO) {
                    reset_btn.setBackgroundResource(R.drawable.button_background_border_white);
                    reset_btn.setTextColor(getResources().getColor(R.color.white));
                } else {
                    reset_btn.setBackgroundResource(R.drawable.button_background_border_black);
                    reset_btn.setTextColor(getResources().getColor(R.color.black));
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

    private void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            otp.setEnabled(false);
        }
    }

    private void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            otp.setEnabled(true);
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
        AuthHandler.handleFailed(getContext(), message);
    }
}