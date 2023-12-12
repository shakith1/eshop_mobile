package lk.oxo.eshop.util.cart;

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
import lk.oxo.eshop.model.CartItem;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private CartTotalListener listener;
    private List<CartItem> cartItems;
    private Context context;

    public CartAdapter(List<CartItem> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    public void getTotalPrice() {
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        listener.onCalculated(totalPrice);
//        return totalPrice;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView title, price, qty;
        private ImageView imageView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView39);
            price = itemView.findViewById(R.id.textView40);
            qty = itemView.findViewById(R.id.textView45);
            imageView = itemView.findViewById(R.id.imageViewCart);
        }
    }
}
