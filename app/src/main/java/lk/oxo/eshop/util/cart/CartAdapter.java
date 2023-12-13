package lk.oxo.eshop.util.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import lk.oxo.eshop.R;
import lk.oxo.eshop.components.cart.UserCart_Empty;
import lk.oxo.eshop.components.cart.UserCart_Products_Main;
import lk.oxo.eshop.model.CartItem;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private UserCart_Products_Main main;
    private List<CartItem> cartItems;
    private Context context;

    public CartAdapter(List<CartItem> cartItems, Context context, UserCart_Products_Main main) {
        this.cartItems = cartItems;
        this.context = context;
        this.main = main;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return totalPrice;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartItem cartItem = cartItems.get(position);

        holder.title.setText(cartItem.getProduct().getTitle());
        holder.price.setText(context.getString(R.string.currency) + " " + String.valueOf(cartItem.getProduct().getPrice()) + "0");
        holder.qty.setText(context.getString(R.string.quantity) + ": " + String.valueOf(cartItem.getQuantity()));
        List<Uri> images = cartItem.getProduct().getImages();

        Picasso.get()
                .load(Uri.parse(String.valueOf(cartItem.getProduct().getImages().get(images.size() / 2))))
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemChanged(position, cartItems.size());
                CartHelper cartHelper = new CartHelper(context);
                cartHelper.removeCart(cartItem);
                main.updateTotal(getTotalPrice());
                if (cartItems.isEmpty()) {
                    main.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView3, UserCart_Empty.class, null)
                            .commit();
                }
            }
        });

        if (position == cartItems.size() - 1) {
            main.updateTotal(getTotalPrice());
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView title, price, qty, remove;
        private ImageView imageView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView39);
            price = itemView.findViewById(R.id.textView40);
            qty = itemView.findViewById(R.id.textView45);
            imageView = itemView.findViewById(R.id.imageViewCart);
            remove = itemView.findViewById(R.id.textView46);
        }
    }
}
