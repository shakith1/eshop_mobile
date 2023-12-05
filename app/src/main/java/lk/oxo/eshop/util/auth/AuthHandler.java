package lk.oxo.eshop.util.auth;

import android.content.Context;
import android.widget.Toast;

public class AuthHandler {
    public static void handleSuccess(){

    }
    public static void handleFailed(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
