package lk.oxo.eshop.product;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import lk.oxo.eshop.R;
import lk.oxo.eshop.components.cart.UserCart;
import lk.oxo.eshop.model.CartItem;
import lk.oxo.eshop.model.Product;
import lk.oxo.eshop.util.LoggedUser;
import lk.oxo.eshop.util.ProgressBarInterface;
import lk.oxo.eshop.util.cart.CartHelper;
import lk.oxo.eshop.util.product.ProductHelper;
import lk.oxo.eshop.util.product.ProductImageViewAdapter;
import lk.oxo.eshop.util.product.SingleProductRecieveCallback;

public class SingleProductView extends Fragment implements ProgressBarInterface {
    private Spinner spinner;
    private ViewPager2 pager;
    private ProductImageViewAdapter imageViewAdapter;
    private Product productMain;
    private TextView title, price, quantity, description;
    private Product product_;
    private Button cart;
    private ProgressBar progressBar;
    private ImageView cartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_product_view, container, false);

        spinner = view.findViewById(R.id.spinner);
        pager = view.findViewById(R.id.pager);
        title = view.findViewById(R.id.textView31);
        price = view.findViewById(R.id.textView32);
        description = view.findViewById(R.id.textView42);
        quantity = view.findViewById(R.id.textView38);
        progressBar = view.findViewById(R.id.progressBar13);

        showProgressBar();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String id = bundle.getString(getString(R.string.product_id));
            if (!id.isEmpty()) {
                ProductHelper helper = new ProductHelper(getContext());
                helper.retrieveProductById(id, new SingleProductRecieveCallback() {
                    @Override
                    public void onRecieved(Product product) {
                        product_ = product;
                        ArrayList<Uri> imagelist = new ArrayList<>();

                        List<Uri> images = product.getImages();
                        int startIndex = images.size() / 2;

                        for (int i = startIndex; i < images.size(); i++) {
                            imagelist.add(Uri.parse(String.valueOf(images.get(i))));
                        }

                        imageViewAdapter = new ProductImageViewAdapter(imagelist);
                        pager.setAdapter(imageViewAdapter);

                        title.setText(product.getTitle());
                        price.setText(getContext().getString(R.string.currency) + " " + product.getPrice() + "0");
                        description.setText(product.getDescription());
                        quantity.setText(String.valueOf(product.getQuantity()));

//                      Custom Spinner Values

                        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner);
                        for (int i = 0; i < product.getQuantity(); i++)
                            adapter.add(getString(R.string.quantity) + ": " + (i + 1));

                        adapter.setDropDownViewResource(R.layout.custom_spinner_list);
                        spinner.setAdapter(adapter);

                        hideProgressBar();
                    }
                });
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cart = view.findViewById(R.id.button23);
        cartView = view.findViewById(R.id.imageView10);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoggedUser.isUserLogged()) {
                    String selectedQty = spinner.getSelectedItem().toString();
                    CartItem cartItem = new CartItem(product_, Long.parseLong(selectedQty.substring(selectedQty.indexOf(":") + 2)));
                    CartHelper helper = new CartHelper(cartItem, getContext());
                    helper.saveCart(new CartHelper.OnCartSavedCallback() {
                        @Override
                        public void onSaved() {
                            Snackbar snackbar = Snackbar.make(view, "Product added to cart", Snackbar.LENGTH_SHORT);

                            snackbar.setAction("View Cart", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    });
                } else {
                    LoggedUser.loadLogin(getActivity());
                }
            }
        });

        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(new UserCart());
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