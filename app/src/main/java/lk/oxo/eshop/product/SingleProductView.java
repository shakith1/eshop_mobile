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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.Product;
import lk.oxo.eshop.util.product.ProductImageViewAdapter;
import lk.oxo.eshop.util.product.SingleProductRecieveCallback;

public class SingleProductView extends Fragment {
    private Spinner spinner;
    private ViewPager2 pager;
    private ProductImageViewAdapter imageViewAdapter;
    private Product productMain;
    private TextView title,price,quantity,description;

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

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner);
        adapter.add(getString(R.string.quantity));

        adapter.setDropDownViewResource(R.layout.custom_spinner_list);
        spinner.setAdapter(adapter);

        Bundle bundle = getArguments();
        if(bundle != null){
            String id = bundle.getString(getString(R.string.product_id));
            if(!id.isEmpty()){
                ProductHelper helper = new ProductHelper(getContext());
                helper.retrieveProductById(id, new SingleProductRecieveCallback() {
                    @Override
                    public void onRecieved(Product product) {
                        ArrayList<Uri> imagelist = new ArrayList<>();

                        List<Uri> images = product.getImages();
                        int startIndex = images.size() / 2;

                        for(int i=startIndex;i<images.size();i++){
                            imagelist.add(Uri.parse(String.valueOf(images.get(i))));
                        }

                        imageViewAdapter = new ProductImageViewAdapter(imagelist);
                        pager.setAdapter(imageViewAdapter);

                        title.setText(product.getTitle());
                        price.setText(getContext().getString(R.string.currency)+" "+product.getPrice()+"0");
                        description.setText(product.getDescription());
                        quantity.setText(String.valueOf(product.getQuantity()));
                    }
                });
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//
//
////        Header Image
//        ArrayList<String> imagelist = new ArrayList<>();
//        imagelist.add("https://picsum.photos/id/237/200/300");
//        imagelist.add("https://picsum.photos/seed/picsum/200/300");
//
//        imageViewAdapter = new ProductImageViewAdapter(imagelist);
//        pager.setAdapter(imageViewAdapter);
    }
}