package lk.oxo.eshop.Signup;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;

import lk.oxo.eshop.R;
import lk.oxo.eshop.util.AuthenticationManager;
import lk.oxo.eshop.util.Reciever;
import lk.oxo.eshop.util.SmsListener;
import lk.oxo.eshop.util.UIMode;
import lk.oxo.eshop.util.Validation;

public class Signup_Verify_OTP extends Fragment implements SmsListener {

    private static final long COUNTDOWN_DURATION = 30 * 1000;
    private static final long INTERVAL = 1000;
    private CountDownTimer countDownTimer;
    private TextView resendText, mobileText;
    private EditText otp;
    private Button reset_btn;
    private String email, firstName, lastName, password, mobile;
    private Bundle userData;
    private AuthenticationManager authenticationManager;

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

        Button continue_btn = view.findViewById(R.id.button19);
        otp = view.findViewById(R.id.pinView);

        resendText = view.findViewById(R.id.textView22);
        mobileText = view.findViewById(R.id.textView21);
        reset_btn = view.findViewById(R.id.button20);

        Bundle data = getArguments();

        if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO) {
            continue_btn.setBackgroundResource(R.drawable.button_background_continue_night_disable);
            reset_btn.setBackgroundResource(R.drawable.button_background_continue_night_disable);
        }

        if (data != null) {
            userData = data.getBundle(getString(R.string.user_bundle));

            mobile = data.getString(getString(R.string.mobile_bundle));

            email = userData.getString(getString(R.string.email_bundle));
            firstName = userData.getString(getString(R.string.fname_bundle));
            lastName = userData.getString(getString(R.string.lname_bundle));
            password = userData.getString(getString(R.string.password_bundle));

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
}