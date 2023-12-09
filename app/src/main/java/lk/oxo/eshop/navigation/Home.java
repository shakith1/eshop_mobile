package lk.oxo.eshop.navigation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lk.oxo.eshop.R;
import lk.oxo.eshop.Signup.Create_Account;
import lk.oxo.eshop.components.Purchases;
import lk.oxo.eshop.components.UserCart;
import lk.oxo.eshop.components.WatchList;
import lk.oxo.eshop.model.Product;
import lk.oxo.eshop.model.User;
import lk.oxo.eshop.product.ProductHelper;
import lk.oxo.eshop.product.SingleProductView;
import lk.oxo.eshop.util.LoggedUser;
import lk.oxo.eshop.util.LoginPreferences;
import lk.oxo.eshop.util.product.ProductAdapter;
import lk.oxo.eshop.util.product.ProductRecieveCallback;


public class Home extends Fragment implements ProductAdapter.OnItemClickListener{
private RecyclerView recyclerView;
    private SearchView searchView;
    private ImageView cart;
    private List<Product> product;
    private ProductAdapter.OnItemClickListener listener = this;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.montserrat);
    searchView = view.findViewById(R.id.searchView);
    recyclerView = view.findViewById(R.id.recyclerView);
    cart = view.findViewById(R.id.imageView4);

//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View searchViewLayout = inflater.inflate(R.layout.custom_search, null);
//searchView.setCus
////        Typeface fromAsset = Typeface.createFromAsset(getActivity().getAssets(), "font/montserrat_regular.ttf");
////        EditText searchSrc = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
////        searchSrc.setTypeface(fromAsset);

        ProductHelper helper = new ProductHelper(getContext());
        helper.retrieveProducts(new ProductRecieveCallback() {
            @Override
            public void onRecieved(List<Product> productList) {
                product = productList;

                ProductAdapter adapter = new ProductAdapter(product,listener);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                recyclerView.setAdapter(adapter);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(new UserCart());
            }
        });
    }

    private void handleLogin(Fragment fragment){
        if(!LoggedUser.isUserLogged())
            LoggedUser.loadLogin(getActivity());
        else{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView2, fragment,null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onItemClick(String productId) {
        SingleProductView singleProductView = new SingleProductView();

        Bundle bundle = new Bundle();
        bundle.putString(getContext().getString(R.string.product_id),productId);
        singleProductView.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView2, singleProductView,null)
                .addToBackStack(null)
                .commit();
    }
}