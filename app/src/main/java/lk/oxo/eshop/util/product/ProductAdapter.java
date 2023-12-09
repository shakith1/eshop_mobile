package lk.oxo.eshop.util.product;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private OnItemClickListener listener;

    public ProductAdapter(List<Product> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(String productId);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView title;
        TextView price;
        ConstraintLayout mainProductView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imageView5);
            title = itemView.findViewById(R.id.textView33);
            price = itemView.findViewById(R.id.textView43);
            mainProductView = itemView.findViewById(R.id.mainProductView);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_main, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder,int position) {
        Product product = productList.get(position);
        List<Uri> images = product.getImages();
        
        Picasso.get()
                .load(Uri.parse(String.valueOf(images.get(2))))
                .fit().centerCrop()
                .into(holder.productImage);
        holder.title.setText(product.getTitle());
        holder.price.setText(String.valueOf(product.getPrice()));

        holder.mainProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onItemClick(product.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
