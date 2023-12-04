package lk.oxo.eshop.util;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class AuthenticationManager implements Serializable {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public PhoneAuthProvider.ForceResendingToken getResendingToken() {
        return resendingToken;
    }

    public void setResendingToken(PhoneAuthProvider.ForceResendingToken resendingToken) {
        this.resendingToken = resendingToken;
    }

    public void createAccountAuth(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                        }
                    }
                });
    }

    public void createAccount(String mobile, Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber("+94" + mobile)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
