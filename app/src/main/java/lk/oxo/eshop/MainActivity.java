package lk.oxo.eshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import lk.oxo.eshop.model.FirebaseUser;
import lk.oxo.eshop.model.User;
import lk.oxo.eshop.navigation.Home;
import lk.oxo.eshop.navigation.Inbox;
import lk.oxo.eshop.navigation.SellerMain;
import lk.oxo.eshop.navigation.UserMain;
import lk.oxo.eshop.util.EmailSender;
import lk.oxo.eshop.util.LoggedUser;
import lk.oxo.eshop.util.LoginPreferences;

public class MainActivity extends AppCompatActivity {
private LoginPreferences loginPreferences;
    private NavigationBarView navigationView;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottomNavigationView);
        loginPreferences = new LoginPreferences(this);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(0);
        }

        user = loginPreferences.getUser();
        LoggedUser.setLoggedUser(user);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment view = null;

                if(item.getItemId() == R.id.bottomNavHome)
                    view = new Home();
                else  if(item.getItemId() == R.id.bottomNavUser)
                    view = new UserMain();
                else  if(item.getItemId() == R.id.bottomNavInbox)
                    view = new Inbox();
                else  if(item.getItemId() == R.id.bottomNavSeller)
                    view = new SellerMain();

                if(view != null){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView2,view).commit();
                    return true;
                }
                return false;
            }
        });
    }
}