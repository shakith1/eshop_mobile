package lk.oxo.eshop.components.cart;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.CartItem;
import lk.oxo.eshop.util.cart.CartAdapter;
import lk.oxo.eshop.util.cart.CartHelper;
import lk.oxo.eshop.util.cart.CartTotalListener;

public class UserCart_Products extends Fragment {
    private RecyclerView cartView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_cart__products, container, false);
        cartView = view.findViewById(R.id.recyclerViewCart);
        CartHelper helper = new CartHelper(getContext());
        helper.getCartDetails(new CartHelper.OnCartDataCallback() {
            @Override
            public void onLoad(List<CartItem> cartItems) {
                CartAdapter adapter = new CartAdapter(cartItems, getContext(),(UserCart_Products_Main) getParentFragment());
                cartView.setLayoutManager(new LinearLayoutManager(getContext()));
                cartView.setAdapter(adapter);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}