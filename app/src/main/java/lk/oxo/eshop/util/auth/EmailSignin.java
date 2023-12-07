package lk.oxo.eshop.util.auth;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import lk.oxo.eshop.R;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.Preferences;
import lk.oxo.eshop.util.Validation;
import lk.oxo.eshop.util.auth.google.FirebaseValidationCallback;

public class EmailSignin {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private ProgressBarInterface progressBarInterface;
    private Context context;
    private String email,password;
    private boolean check;

    public EmailSignin(ProgressBarInterface progressBarInterface, Context context,
                       String email,String password, boolean check) {
        this.progressBarInterface = progressBarInterface;
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.email = email;
        this.password = password;
        this.check = check;
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void chooseSignInMethod(){
        progressBarInterface.showProgressBar();
        if(Validation.checkEmail(email))
            signinUserEmail();
        else if(Validation.checkMobile(email))
            signinUserMobile(new FirebaseValidationCallback() {
                @Override
                public void onResult(boolean exists, String authentication) {
                    if(exists){
                        email = authentication;
                        signinUserEmail();
                    }
                }
            });
    }

    private void signinUserMobile(FirebaseValidationCallback callback){
        CollectionReference users = firestore.collection(context.getString(R.string.users));
        Query query = users.whereEqualTo(context.getString(R.string.mobile_collection), email);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            boolean exists = result != null && !result.isEmpty();
                            if (exists) {
                                DocumentSnapshot snapshot = result.getDocuments().get(0);
                                String email = snapshot.getString(context.getString(R.string.email_collection));
                                callback.onResult(true, email);
                            } else
                                callback.onResult(false, null);
                        } else
                            callback.onResult(false, null);
                    }
                });
    }
    public void signinUserEmail(){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBarInterface.hideProgressBar();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user!=null){
                                if(check)
                                    Preferences.storeData(email,password,context);
                                AuthHandler.handleSuccess(context);
                            }else{
                                progressBarInterface.hideProgressBar();
                            }
                        }else{
                            progressBarInterface.hideProgressBar();
                            progressBarInterface.showErrorMessage();
                        }
                    }
                });
    }
}
