package lk.oxo.eshop.util.auth;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import lk.oxo.eshop.MainActivity;

public class AuthHandler {
    public static void handleSuccess(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
    public static void handleFailed(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
