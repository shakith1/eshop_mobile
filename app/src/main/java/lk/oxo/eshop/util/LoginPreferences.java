package lk.oxo.eshop.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.FirebaseUser;
import lk.oxo.eshop.model.User;

public class LoginPreferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public LoginPreferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getString(R.string.user_preferences), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void storeUser(FirebaseUser user) {
        editor.putBoolean(context.getString(R.string.is_user_logged), true);
        editor.putString(context.getString(R.string.uid_bundle), user.getUid());
        editor.putString(context.getString(R.string.email_bundle), user.getEmail());
        editor.putString(context.getString(R.string.fname_bundle), user.getFname());
        editor.putString(context.getString(R.string.lname_bundle), user.getLname());
        editor.putString(context.getString(R.string.mobile_bundle), user.getMobile());

        editor.apply();
    }

    private boolean checkLogin() {
        boolean isLogged = preferences.getBoolean(context.getString(R.string.is_user_logged), false);
        if (isLogged)
            return true;
        return false;
    }

    public FirebaseUser getUser() {
        if(checkLogin()) {
            FirebaseUser user = new FirebaseUser();
            user.setUid(preferences.getString(context.getString(R.string.uid_bundle),null));
            user.setEmail(preferences.getString(context.getString(R.string.email_bundle),null));
            user.setFname(preferences.getString(context.getString(R.string.fname_bundle),null));
            user.setLname(preferences.getString(context.getString(R.string.lname_bundle),null));
            user.setMobile(preferences.getString(context.getString(R.string.mobile_bundle),null));

            return user;
        }else
            return null;
    }

    public void logOutUser(){
        FirebaseAuth.getInstance().signOut();
        editor.clear();
        editor.apply();
    }
}
