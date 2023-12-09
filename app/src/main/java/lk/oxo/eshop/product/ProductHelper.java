package lk.oxo.eshop.product;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.Product;
import lk.oxo.eshop.util.product.ProductRecieveCallback;
import lk.oxo.eshop.util.product.SingleProductRecieveCallback;

public class ProductHelper {
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private Product product;
    private Context context;

    public ProductHelper(Context context) {
        this.firestore = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.context = context;
    }

    public void retrieveProductById(String id, SingleProductRecieveCallback callback){
        DocumentReference productRef = firestore.collection(context.getString(R.string.products_firebase)).document(id);
        productRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document.exists()) {
                    Product product = new Product();
                    product.setId(document.getId());
                    product.setTitle(document.getString("title"));
                    product.setDescription(document.getString("description"));
                    product.setPrice(Double.parseDouble(document.getString("price")));
                    product.setQuantity(document.getLong("quantity"));

                    List<Uri> images = (List<Uri>) document.get("images");
                    if (images != null && !images.isEmpty()) {
                        product.setImages(images);
                    }
                    callback.onRecieved(product);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors while fetching data
            }
        });
    }

    public void retrieveProducts(ProductRecieveCallback callback) {
        CollectionReference productCollection = firestore
                .collection(context.getString(R.string.products_firebase));
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
                        product.setId(document.getId());
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
