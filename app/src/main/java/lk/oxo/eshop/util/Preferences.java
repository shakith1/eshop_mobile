package lk.oxo.eshop.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import lk.oxo.eshop.R;

public class Preferences {
    public static void storeData(String email,String password,Context context){

//        Encryption encryption = new Encryption(context);
        SharedPreferences preferences = context
                .getSharedPreferences(context.getString(R.string.preference_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.email_bundle),email);
//        editor.putString(context.getString(R.string.password_bundle),encryption.encrypt(password).toString());
        editor.putString(context.getString(R.string.password_bundle),password);
        editor.apply();
    }

    public static HashMap<String,String> retrieveData(Context context){
        Encryption encryption = new Encryption(context);
        SharedPreferences preferences = context
                .getSharedPreferences(context.getString(R.string.preference_name), Context.MODE_PRIVATE);
        String email = preferences.getString(context.getString(R.string.email_bundle), null);

            String encryptedPassword = preferences.
                    getString(context.getString(R.string.password_bundle), null);
//        if(encryptedPassword != null) {
//            String password = encryption.decrypt(encryptedPassword);

            HashMap<String, String> map = new HashMap<>();
            map.put(context.getString(R.string.email_bundle), email);
            map.put(context.getString(R.string.password_bundle), encryptedPassword);

            return map;
//        }else
//            return null;
    }

}
