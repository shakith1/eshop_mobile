package lk.oxo.eshop.util.auth.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import lk.oxo.eshop.R;
import lk.oxo.eshop.Signup.Create_Account;
import lk.oxo.eshop.Signup.Create_Account_Google;
import lk.oxo.eshop.Signup.LinkAccount;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.auth.AuthHandler;
import lk.oxo.eshop.util.auth.google.FirebaseValidationCallback;

public class GoogleSignIn {
    private SignInClient signInClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private Context context;
    private ProgressBarInterface progressBarInterface;
    private Fragment fragment;

    public GoogleSignIn(SignInClient signInClient, Context context, ProgressBarInterface progressBarInterface
                        ,Fragment fragment) {
        this.signInClient = signInClient;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        this.progressBarInterface = progressBarInterface;
        this.fragment = fragment;
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
        progressBarInterface.showProgressBar();
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        checkUserExists(user.getEmail(), new FirebaseValidationCallback() {
                            @Override
                            public void onResult(boolean exists, String authentication) {
                                if (exists) {
                                    if (authentication.equals(context.getString(R.string.authentication_phone))) {
                                        progressBarInterface.hideProgressBar();
                                        showLinkAccount(user.getEmail());
                                    } else if (authentication.equals(context.getString(R.string.authentication_google))) {
                                        progressBarInterface.hideProgressBar();
                                        AuthHandler.handleSuccess(context);
                                    }
                                } else {
                                    showCreateAccount(user.getUid(), user.getEmail(), user.getDisplayName());
                                }
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarInterface.hideProgressBar();
            }
        });
    }

    private void checkAuthenticationMethod(String uid, AuthenticationCallback callback) {

    }

    private void showLinkAccount(String email) {
        Bundle data = new Bundle();
        data.putString(context.getString(R.string.email_bundle), email);

        LinkAccount linkAccount = new LinkAccount();
        linkAccount.setArguments(data);

        fragment.getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, linkAccount, null)
                .addToBackStack(null)
                .commit();
    }

    private void showCreateAccount(String uid, String email, String displayName) {
        String[] strings = displayName.split(" ");
        String first_name = "", last_name = "";

        if (strings.length >= 2) {
            first_name = strings[0];
            last_name = strings[strings.length - 1];
        }

        Bundle data = new Bundle();
        data.putString(context.getString(R.string.uid_bundle), uid);
        data.putString(context.getString(R.string.email_bundle), email);
        data.putString(context.getString(R.string.fname_bundle), first_name);
        data.putString(context.getString(R.string.lname_bundle), last_name);

        Create_Account_Google create_account_google = new Create_Account_Google();
        create_account_google.setArguments(data);

        fragment.getActivity().getSupportFragmentManager().beginTransaction()
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
                            if (exists) {
                                DocumentSnapshot snapshot = result.getDocuments().get(0);
                                String authentication = snapshot.getString(context.getString(R.string.authentication));
                                callback.onResult(true, authentication);
                            } else
                                callback.onResult(false, null);
                        } else
                            callback.onResult(false, null);
                    }
                });
    }

}
