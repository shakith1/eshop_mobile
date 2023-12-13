package lk.oxo.eshop.components.cart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import lk.oxo.eshop.R;
import lk.oxo.eshop.Signup.Signup_email;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.cart.CartHelper;

public class UserCart extends Fragment implements ProgressBarInterface {
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_cart, container, false);
        progressBar = view.findViewById(R.id.progressBar11);
        showProgressBar();
        CartHelper cartHelper = new CartHelper(getContext());
        cartHelper.checkCart(new CartHelper.OnCartCallback() {
            @Override
            public void onData(boolean data) {
                if (!data) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainerView3, UserCart_Empty.class, null)
                            .commit();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainerView3, UserCart_Products_Main.class, null)
                            .commit();
                }
                hideProgressBar();
            }
        });
        return view;
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void hideErrorMessage() {

    }
}