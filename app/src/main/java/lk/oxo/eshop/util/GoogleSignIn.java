package lk.oxo.eshop.util;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import lk.oxo.eshop.R;
import lk.oxo.eshop.Signup.Create_Account;
import lk.oxo.eshop.Signup.Create_Account_Google;
import lk.oxo.eshop.Signup.LinkAccount;

public class GoogleSignIn {
    private SignInClient signInClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private Context context;

    private Create_Account account;

    public GoogleSignIn(SignInClient signInClient, Context context, Create_Account account) {
        this.signInClient = signInClient;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        this.account = account;
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void handleSignInResult(Intent intent) {
        try {
            SignInCredential signInCredential = signInClient.getSignInCredentialFromIntent(intent);
            String googleIdToken = signInCredential.getGoogleIdToken();
            firebaseAuthWithGoogle(googleIdToken);
        } catch (ApiException e) {

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        account.showProgressBar();
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                    AuthResult result = task.getResult();
                    if (user != null) {
//                        boolean newUser = result.getAdditionalUserInfo().isNewUser();
//                        if(newUser){
//                            showLinkAccount();
//                        }else{
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                        }
                        checkUserExists(user.getEmail(), new FirebaseValidationCallback() {
                            @Override
                            public void onResult(boolean exists) {
                                account.hideProgressBar();
                                if (exists) {
                                    System.out.println(user.getDisplayName());
                                    showLinkAccount();
                                } else {
                            showCreateAccount();
                                }
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void showLinkAccount() {
        LinkAccount linkAccount = new LinkAccount();
        account.getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, linkAccount, null)
                .addToBackStack(null)
                .commit();
    }

    private void showCreateAccount(){
        Create_Account_Google create_account_google = new Create_Account_Google();
        account.getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, create_account_google, null)
                .addToBackStack(null)
                .commit();
    }

    private void checkUserExists(String email, FirebaseValidationCallback callback) {
        CollectionReference users = firestore.collection(context.getString(R.string.users));
        Query query = users.whereEqualTo(context.getString(R.string.email_collection), email);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            boolean exists = result != null && !result.isEmpty();
                            callback.onResult(exists);
                        } else
                            callback.onResult(false);
                    }
                });
    }

}
