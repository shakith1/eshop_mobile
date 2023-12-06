package lk.oxo.eshop.util.auth.google;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import lk.oxo.eshop.R;
import lk.oxo.eshop.Signup.Create_Account_Google;
import lk.oxo.eshop.model.FirebaseUser;
import lk.oxo.eshop.util.auth.AuthHandler;

public class GoogleAuthentication {
    private Create_Account_Google create_account_google;
    private FirebaseFirestore firestore;
    private Context context;

    public GoogleAuthentication(Create_Account_Google create_account_google, Context context) {
        this.create_account_google = create_account_google;
        this.context = context;
    }

    public void createAccount(FirebaseUser user){
        create_account_google.showProgressBar();
       firestore = FirebaseFirestore.getInstance();
        DocumentReference document = firestore.collection(context.getString(R.string.users))
                .document(user.getUid());

        HashMap<String, String> map = new HashMap<>();
        map.put(context.getString(R.string.email_collection),user.getEmail());
        map.put(context.getString(R.string.fname_collection),user.getFname());
        map.put(context.getString(R.string.lname_collection),user.getLname());
        map.put(context.getString(R.string.authentication),context.getString(R.string.authentication_google));

        document.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                create_account_google.hideProgressBar();
               AuthHandler.handleSuccess(context);
            }
        });
    }
}
