package lk.oxo.eshop.components.cart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lk.oxo.eshop.R;
import lk.oxo.eshop.util.cart.CartTotalListener;

public class UserCart_Products_Main extends Fragment implements CartTotalListener {
    private TextView total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_cart__products__main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        total = view.findViewById(R.id.textView50);
    }

    @Override
    public void onCalculated(double totalPrice) {
        total.setText(String.valueOf(totalPrice));
    }
}