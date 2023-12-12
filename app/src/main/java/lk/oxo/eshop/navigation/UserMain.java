package lk.oxo.eshop.navigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import lk.oxo.eshop.MainActivity;
import lk.oxo.eshop.R;
import lk.oxo.eshop.components.Purchases;
import lk.oxo.eshop.components.cart.UserCart;
import lk.oxo.eshop.components.WatchList;
import lk.oxo.eshop.model.User;
import lk.oxo.eshop.util.LoggedUser;
import lk.oxo.eshop.util.LoginPreferences;

public class UserMain extends Fragment {
    private ImageView cart;
    private ConstraintLayout layout;
    private TextView name;
    private Button watchList, cartButton, purchases, logout;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = view.findViewById(R.id.constraintLayout);
        name = view.findViewById(R.id.textView47);

        watchList = view.findViewById(R.id.button21);
        cartButton = view.findViewById(R.id.button24);
        purchases = view.findViewById(R.id.button25);
        logout = view.findViewById(R.id.button26);

        cart = view.findViewById(R.id.imageView6);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoggedUser.isUserLogged()) {
                    LoggedUser.loadLogin(getActivity());
                } else {
                    handleLogin(new UserCart());
                }
            }
        });

        user = LoggedUser.getLoggedUser();
        if (user != null) {
            name.setText(user.getFname() + " " + user.getLname());
        }
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(new UserCart());
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        watchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(new WatchList());
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(new UserCart());
            }
        });

        purchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(new Purchases());
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoggedUser.isUserLogged())
                    LoggedUser.loadLogin(getActivity());
                else
                    showLogoutConfirmation();
            }
        });
    }

    private void handleLogin(Fragment fragment) {
        if (!LoggedUser.isUserLogged())
            LoggedUser.loadLogin(getActivity());
        else {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView2, fragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void handleLogin() {
        if (!LoggedUser.isUserLogged())
            LoggedUser.loadLogin(getActivity());
    }

    private void showLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logout_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginPreferences preferences = new LoginPreferences(getContext());
                        preferences.logOutUser();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}