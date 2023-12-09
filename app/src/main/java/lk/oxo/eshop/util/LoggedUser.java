package lk.oxo.eshop.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import lk.oxo.eshop.Login.Signin_Home;
import lk.oxo.eshop.Login.Signin_main;
import lk.oxo.eshop.MainActivity;
import lk.oxo.eshop.model.FirebaseUser;
import lk.oxo.eshop.model.User;

public class LoggedUser {
    private static FirebaseUser userData;

    public static void setLoggedUser(FirebaseUser user){
       userData = user;
    }

    public static User getLoggedUser(){
        if(userData!= null)
        return userData;
        else return null;
    }

    public static boolean isUserLogged(){
        if(userData != null)
            return true;
        else return false;
    }

    public static void loadLogin(Activity activity){
        Intent intent = new Intent(activity, Signin_Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.getApplicationContext().startActivity(intent);
    }

    public static void loadHome(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.getApplicationContext().startActivity(intent);
    }
}
