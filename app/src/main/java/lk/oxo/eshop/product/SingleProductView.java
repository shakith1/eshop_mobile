package lk.oxo.eshop.product;

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

import java.util.ArrayList;

import lk.oxo.eshop.R;
import lk.oxo.eshop.util.product.ProductImageViewAdapter;

public class SingleProductView extends Fragment {
    private Spinner spinner;
    private ViewPager2 pager;
    private ProductImageViewAdapter imageViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_product_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = view.findViewById(R.id.spinner);
        pager = view.findViewById(R.id.pager);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner);
        adapter.add(getString(R.string.quantity));

        adapter.setDropDownViewResource(R.layout.custom_spinner_list);
        spinner.setAdapter(adapter);

//        Header Image
        ArrayList<String> imagelist = new ArrayList<>();
        imagelist.add("https://picsum.photos/id/237/200/300");
        imagelist.add("https://picsum.photos/seed/picsum/200/300");

        imageViewAdapter = new ProductImageViewAdapter(imagelist);
        pager.setAdapter(imageViewAdapter);
    }
}