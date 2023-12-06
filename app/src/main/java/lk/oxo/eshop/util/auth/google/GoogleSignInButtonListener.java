package lk.oxo.eshop.util.auth.google;

import android.app.PendingIntent;
import android.content.Context;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import lk.oxo.eshop.R;

public class GoogleSignInButtonListener implements View.OnClickListener {
    private Context context;
    private SignInClient signInClient;
    private ActivityResultLauncher<IntentSenderRequest> signInLauncher;

    public GoogleSignInButtonListener(Context context,SignInClient signInClient,ActivityResultLauncher<IntentSenderRequest> signInLauncher) {
        this.context = context;
        this.signInClient = signInClient;
        this.signInLauncher = signInLauncher;
    }

    @Override
    public void onClick(View v) {
        GetSignInIntentRequest intentRequest = GetSignInIntentRequest.builder()
                .setServerClientId(context.getString(R.string.web_client_id)).build();
        Task<PendingIntent> signInIntent = signInClient.getSignInIntent(intentRequest);

        signInIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
            @Override
            public void onSuccess(PendingIntent pendingIntent) {
                IntentSenderRequest build = new
                        IntentSenderRequest.Builder(pendingIntent).build();
                signInLauncher.launch(build);
            }
        });
    }
}
