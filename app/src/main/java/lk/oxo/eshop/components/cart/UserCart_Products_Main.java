package lk.oxo.eshop.components.cart;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import lk.oxo.eshop.components.Delivery;
import lk.oxo.eshop.R;

public class UserCart_Products_Main extends Fragment{
    private TextView total;
    private Button checkout;

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
        checkout = view.findViewById(R.id.button27);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setReorderingAllowed(true)
//                        .replace(R.id.fragmentContainerView2, DeliveryDetails.class, null)
//                        .addToBackStack(null)
//                        .commit();
                startActivity(new Intent(getActivity(), Delivery.class));
            }
        });
    }

    public void updateTotal(double totalPrice){
        total.setText(getContext().getString(R.string.currency)+ " "+totalPrice+"0");
    }
}