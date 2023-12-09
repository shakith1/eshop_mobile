package lk.oxo.eshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.oxo.eshop.util.product.ProductImageViewAdapter;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ViewPager2 pager2 = findViewById(R.id.pager);

//         HorizontalScrollView view = findViewById(R.id.hview);
//         LinearLayout images = findViewById(R.id.imagel);
//
        ArrayList<String> imagelist = new ArrayList<>();
        imagelist.add("https://picsum.photos/id/237/200/300");
        imagelist.add("https://picsum.photos/seed/picsum/200/300");
        imagelist.add("https://picsum.photos/id/237/200/300");
        imagelist.add("https://picsum.photos/id/237/200/300");
        imagelist.add("https://picsum.photos/id/237/200/300");
//        ProductImageViewAdapter adapter = new ProductImageViewAdapter(imagelist);
//        pager2.setAdapter(adapter);
////        imagelist.add("R.drawable.icons8_dropdown_40");
////        imagelist.add("R.drawable.icons8_dropdown_40");
//
////        for(String url:imagelist){
//            ImageView v = new ImageView(getApplicationContext());
//            v.setScaleType(ImageView.ScaleType.FIT_XY);
//            Picasso.get().load(R.drawable.download).into(v);
//            images.addView(v);
//        ImageView v1 = new ImageView(getApplicationContext());
//        v1.setScaleType(ImageView.ScaleType.FIT_XY);
//        Picasso.get().load(R.drawable.download).into(v1);
//        images.addView(v1);
//        }
    }
}