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

import lk.oxo.eshop.Login.Signin;
import lk.oxo.eshop.R;
import lk.oxo.eshop.model.User;
import lk.oxo.eshop.util.LoginPreferences;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.Preferences;
import lk.oxo.eshop.util.SignInCallback;
import lk.oxo.eshop.util.Validation;
import lk.oxo.eshop.util.auth.google.FirebaseValidationCallback;

public class EmailSignin {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private ProgressBarInterface progressBarInterface;
    private Context context;
    private String email,password;
    private boolean check;
    private Signin signin;

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

    public void chooseSignInMethod(Signin signin){
        this.signin = signin;
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
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user!=null){
                                    getUserData(email, new SignInCallback() {
                                        @Override
                                        public void onResult(User user) {
                                            if(user !=null) {
                                                progressBarInterface.hideProgressBar();
                                                LoginPreferences login = new LoginPreferences(context);
                                                login.storeUser(user);
                                                if (check)
                                                    Preferences.storeData(email, password, context);
                                                AuthHandler.handleSuccess(context);
                                            }else{
                                                progressBarInterface.hideProgressBar();
                                                signin.showErrorMessage();
                                            }
                                        }
                                    });
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

    private void getUserData(String email, SignInCallback callback){
        CollectionReference users = firestore.collection(context.getString(R.string.users));
        Query query = users.whereEqualTo(context.getString(R.string.email_collection), email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot result = task.getResult();
                    if(!result.getDocuments().isEmpty()) {
                        DocumentSnapshot snapshot = result.getDocuments().get(0);

                        User user = new User();
                        user.setEmail(snapshot.getString(context.getString(R.string.email_collection)));
                        user.setFname(snapshot.getString(context.getString(R.string.fname_collection)));
                        user.setLname(snapshot.getString(context.getString(R.string.lname_collection)));
                        user.setMobile(snapshot.getString(context.getString(R.string.mobile_collection)));

                        callback.onResult(user);
                    }else callback.onResult(null);
                }
            }
        });
    }
}
