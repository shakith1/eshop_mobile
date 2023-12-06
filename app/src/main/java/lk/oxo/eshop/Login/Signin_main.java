package lk.oxo.eshop.Login;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;

import lk.oxo.eshop.Signup.Create_Account;
import lk.oxo.eshop.R;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.auth.google.GoogleSignIn;
import lk.oxo.eshop.util.auth.google.GoogleSignInButtonListener;

public class Signin_main extends Fragment implements ProgressBarInterface {
    private GoogleSignInButtonListener listener;
    private SignInClient signInClient;
    private Context context;
    private ProgressBar progressBar;
    Button email,google,create;
    private Signin_main signinMain = this;

    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            GoogleSignIn googleSignIn = new GoogleSignIn(signInClient, context,signinMain,signinMain);
                            googleSignIn.handleSignInResult(result.getData());
                        }
                    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        signInClient = Identity.getSignInClient(context);
        return inflater.inflate(R.layout.fragment_signin_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.button5);
        google = view.findViewById(R.id.button2);
        create = view.findViewById(R.id.button);

        progressBar = view.findViewById(R.id.progressBar5);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags != Configuration.UI_MODE_NIGHT_NO) {
            view.findViewById(R.id.button2).setBackgroundResource(R.drawable.button_background_night);
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, Create_Account.class,null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, Signin.class,null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        listener = new GoogleSignInButtonListener(getContext(), signInClient, signInLauncher);
        google.setOnClickListener(listener);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        email.setEnabled(false);
        google.setEnabled(false);
        create.setEnabled(false);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        email.setEnabled(true);
        google.setEnabled(true);
        create.setEnabled(true);
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void hideErrorMessage() {

    }
}