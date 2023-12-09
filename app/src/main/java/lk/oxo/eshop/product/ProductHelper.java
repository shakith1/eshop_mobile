package lk.oxo.eshop.product;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.Product;
import lk.oxo.eshop.util.product.ProductRecieveCallback;

public class ProductHelper {
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private Product product;
    private Context context;

    public ProductHelper() {
        this.firestore = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
    }

    public void retrieveProducts(ProductRecieveCallback callback) {
        CollectionReference productCollection = firestore
                .collection("products");
        System.out.println(productCollection);
        productCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                System.out.println("ds");
                if (task.isSuccessful()) {
                    List<Product> productList = new ArrayList<>();
                    System.out.println(productList);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product product = new Product();
                        product.setTitle(document.getString("title"));
                        product.setDescription(document.getString("description"));
                        product.setPrice(Double.parseDouble(document.getString("price")));
                        product.setQuantity(document.getLong("quantity"));

                        List<Uri> images = (List<Uri>) document.get("images");
                        if (images != null && !images.isEmpty()) {
                            product.setImages(images);
                        }
                        productList.add(product);
                    }
                    callback.onRecieved(productList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
}
