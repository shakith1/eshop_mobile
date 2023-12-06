package lk.oxo.eshop.util.auth.google;

public interface FirebaseValidationCallback {
    void onResult(boolean exists,String authentication);
}
