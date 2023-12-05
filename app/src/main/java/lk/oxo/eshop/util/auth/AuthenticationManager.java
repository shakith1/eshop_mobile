package lk.oxo.eshop.util.auth;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.User;

public class AuthenticationManager implements Serializable {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    private User user;

    private Context context;

    public AuthenticationManager(User user) {
        this.user = user;
    }

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

    public void createAccountAuth(PhoneAuthCredential credential, PhoneAuthCallback callback,Context context) {
        this.context = context;
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();

                            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getFname() + " " + user.getLname())
                                    .build();

                            firebaseUser.updateProfile(changeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                DocumentReference users = db
                                                        .collection(context.getString(R.string.users))
                                                        .document(firebaseUser.getUid());

                                                HashMap<String, String> userMap = getUserMap();
                                                users.set(userMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                callback.authSuccess();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                callback
                                                                        .authFailed(context.getString(R.string.auth_fail_1));
                                                            }
                                                        });
                                            }else
                                                callback.authFailed(context.getString(R.string.auth_fail_2));
                                        }
                                    });
                        }else{
                            callback.authFailed(context.getString(R.string.auth_fail_3));
                        }
                    }
                });
    }

    private HashMap<String , String> getUserMap(){
        HashMap<String , String> userMap = new HashMap<>();
        userMap.put(context.getString(R.string.email_collection), user.getEmail());
        userMap.put(context.getString(R.string.fname_collection), user.getFname());
        userMap.put(context.getString(R.string.lname_collection), user.getLname());
        userMap.put(context.getString(R.string.mobile_collection), user.getMobile());

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

    public void callAuth(PhoneAuthCallback callback,String otp,Context context){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        createAccountAuth(credential,callback,context);
    }
}
