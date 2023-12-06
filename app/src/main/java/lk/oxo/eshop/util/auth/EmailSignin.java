package lk.oxo.eshop.util.auth;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lk.oxo.eshop.util.ProgressBarInterface;

public class EmailSignin {
    private FirebaseAuth firebaseAuth;
    private ProgressBarInterface progressBarInterface;
    private Context context;

    public EmailSignin(ProgressBarInterface progressBarInterface, Context context) {
        this.progressBarInterface = progressBarInterface;
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signinUser(String email, String Password){
        progressBarInterface.showProgressBar();
        firebaseAuth.signInWithEmailAndPassword(email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBarInterface.hideProgressBar();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user!=null){
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
