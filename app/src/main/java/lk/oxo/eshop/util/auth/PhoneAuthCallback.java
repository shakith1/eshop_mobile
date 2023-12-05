package lk.oxo.eshop.util.auth;

import com.google.firebase.auth.PhoneAuthProvider;

public interface PhoneAuthCallback {
    void authSuccess();
    void authFailed(String message);
}
