package lk.oxo.eshop.Signup;

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

import lk.oxo.eshop.R;
import lk.oxo.eshop.util.auth.google.GoogleSignIn;
import lk.oxo.eshop.util.auth.google.GoogleSignInButtonListener;
import lk.oxo.eshop.util.ProgressBarInterface;

public class Create_Account extends Fragment implements ProgressBarInterface {

    private GoogleSignInButtonListener listener;
    private SignInClient signInClient;
    private Context context;
    private ProgressBar progressBar;
    private Create_Account account = this;
    private Button continue_email,google,business_account;
    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            GoogleSignIn googleSignIn = new GoogleSignIn(signInClient, context,account,account);
                            googleSignIn.handleSignInResult(result.getData());
                        }
                    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        signInClient = Identity.getSignInClient(context);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create__account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar6);
        continue_email = view.findViewById(R.id.button9);
        google = view.findViewById(R.id.button8);
        business_account = view.findViewById(R.id.button7);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags != Configuration.UI_MODE_NIGHT_NO) {
            view.findViewById(R.id.button8).setBackgroundResource(R.drawable.button_background_night);
        }

        continue_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, Signup_email.class, null)
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
        continue_email.setEnabled(false);
        google.setEnabled(false);
        business_account.setEnabled(false);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        continue_email.setEnabled(true);
        google.setEnabled(true);
        business_account.setEnabled(true);
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void hideErrorMessage() {

    }
}