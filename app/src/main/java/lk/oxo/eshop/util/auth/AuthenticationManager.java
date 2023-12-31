package lk.oxo.eshop.util.auth;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import lk.oxo.eshop.model.User;
import lk.oxo.eshop.util.EmailSender;
import lk.oxo.eshop.util.LoginPreferences;
import lk.oxo.eshop.R;

public class AuthenticationManager {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private User user;
    private Context context;
    private static AuthenticationManager manager;
    private FirebaseUser firebaseUser;

    private AuthenticationManager(User user) {
        this.user = user;
    }

    public static AuthenticationManager getInstance(User user) {
        manager = new AuthenticationManager(user);
        return manager;
    }

    public static AuthenticationManager getInstance() {
        return manager;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public void setResendingToken(PhoneAuthProvider.ForceResendingToken resendingToken) {
        this.resendingToken = resendingToken;
    }

    public void createAccountAuth(PhoneAuthCredential credential, PhoneAuthCallback callback, Context context) {
        this.context = context;
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = task.getResult().getUser();

                            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getFname() + " " + user.getLname())
                                    .build();

                            firebaseUser.updateProfile(changeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                DocumentReference users = db
                                                        .collection(context.getString(R.string.users))
                                                        .document(firebaseUser.getUid());

                                                HashMap<String, String> userMap = getUserMap();
                                                users.set(userMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                LoginPreferences loginPreferences = new LoginPreferences(context);
                                                                loginPreferences.storeUser(getUser(userMap));
                                                                AuthCredential credential1 = EmailAuthProvider.getCredential(user.getEmail(), user.getPassword());
                                                                firebaseUser.linkWithCredential(credential1)
                                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                new EmailSender(user.getEmail(), user.getFname(), context).execute();
                                                                                callback.authSuccess();
                                                                            }
                                                                        });
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                callback
                                                                        .authFailed(context.getString(R.string.auth_fail_1));
                                                            }
                                                        });
                                            } else
                                                callback.authFailed(context.getString(R.string.auth_fail_2));
                                        }
                                    });
                        } else {
                            callback.authFailed();
                        }
                    }
                });
    }

    private lk.oxo.eshop.model.FirebaseUser getUser(HashMap<String,String> map){
        lk.oxo.eshop.model.FirebaseUser user1 = new lk.oxo.eshop.model.FirebaseUser();
        user1.setUid(firebaseUser.getUid());
        user1.setEmail(map.get(context.getString(R.string.email_collection)));
        user1.setFname(map.get(context.getString(R.string.fname_collection)));
        user1.setLname(map.get(context.getString(R.string.lname_collection)));
        user1.setMobile(map.get(context.getString(R.string.mobile_collection)));

        return user1;
    }
    private HashMap<String, String> getUserMap() {
        HashMap<String, String> userMap = new HashMap<>();

        userMap.put(context.getString(R.string.email_collection), user.getEmail());
        userMap.put(context.getString(R.string.fname_collection), user.getFname());
        userMap.put(context.getString(R.string.lname_collection), user.getLname());
        userMap.put(context.getString(R.string.mobile_collection), user.getMobile());
        userMap.put(context.getString(R.string.authentication),
                context.getString(R.string.authentication_phone));

        return userMap;
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

    public void callAuth(PhoneAuthCallback callback, String otp, Context context) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        createAccountAuth(credential, callback, context);
    }

    public void resendOtp(Activity activity) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setForceResendingToken(resendingToken)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setPhoneNumber(user.getMobile())
                .setActivity(activity)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        resendingToken = forceResendingToken;
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
