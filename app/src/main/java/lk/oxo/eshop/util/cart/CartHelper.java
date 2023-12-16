package lk.oxo.eshop.util.cart;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lk.oxo.eshop.R;
import lk.oxo.eshop.model.CartItem;
import lk.oxo.eshop.model.Product;
import lk.oxo.eshop.model.User;
import lk.oxo.eshop.util.LoggedUser;

public class CartHelper {
    private FirebaseFirestore firestore;
    private CartItem cartItem;
    private Context context;

    public CartHelper(Context context) {
        this.context = context;
        this.firestore = FirebaseFirestore.getInstance();
    }

    public CartHelper(CartItem cartItem, Context context) {
        this.cartItem = cartItem;
        this.context = context;
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void saveCart(OnCartSavedCallback callback) {
        DocumentReference document = firestore.collection(context.getString(R.string.users))
                .document(LoggedUser.getLoggedUser().getUid())
                .collection(context.getString(R.string.cart_item_collection)).document();
            document.set(cartItem)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            callback.onSaved();
                        }
                    });
    }

    public void getCartDetails(OnCartDataCallback callback) {
        DocumentReference document = firestore.collection(context.getString(R.string.users))
                .document(LoggedUser.getLoggedUser().getUid());
        document.collection(context.getString(R.string.cart_item_collection)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CartItem> objects = new ArrayList<>();

                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Map<String, Object> data = snapshot.getData();
                            Map<String, Object> productData = (Map<String, Object>) data.get(context.getString(R.string.cart_product));
                            Long qty = (Long) snapshot.get(context.getString(R.string.cart_qty));

                            Product product = convertToProduct(productData);
                            if(product != null){
                                CartItem cartItem = new CartItem(product, qty);
                                System.out.println(cartItem.getProduct().getTitle());
                                objects.add(cartItem);
                            }
                        }
                        callback.onLoad(objects);
                    }
                });
    }

    public void checkCart(OnCartCallback callback){
        DocumentReference document = firestore.collection(context.getString(R.string.users))
                .document(LoggedUser.getLoggedUser().getUid());
        document.collection(context.getString(R.string.cart_item_collection)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty())
                            callback.onData(false);
                        else callback.onData(true);
                    }
                });
    }

    private Product convertToProduct(Map<String, Object> productData){
        String id = (String) productData.get(context.getString(R.string.product_id));
        String title = (String) productData.get(context.getString(R.string.product_title));
        double price = (Double) productData.get(context.getString(R.string.product_price));
        List<Uri> imageList = (List<Uri>) productData.get(context.getString(R.string.product_images));

        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setPrice(price);
        product.setImages(imageList);

        return product;
    }

    public void removeCart(CartItem cartItem){
        DocumentReference document = firestore.collection(context.getString(R.string.users))
                .document(LoggedUser.getLoggedUser().getUid());
        document.collection(context.getString(R.string.cart_item_collection))
                .whereEqualTo(context.getString(R.string.cart_product_product_id),cartItem.getProduct().getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                documentSnapshot.getReference().delete();
                            }
                        }
                    }
                });
    }

    public void removeAllCart(){
        DocumentReference document = firestore.collection(context.getString(R.string.users))
                .document(LoggedUser.getLoggedUser().getUid());
        document.collection(context.getString(R.string.cart_item_collection)).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                documentSnapshot.getReference().delete();
                            }
                        }
                    }
                });
    }

    public interface OnCartDataCallback {
        void onLoad(List<CartItem> cartItems);
    }

    public interface OnCartCallback{
        void onData(boolean data);
    }

    public interface OnCartSavedCallback {
        void onSaved();
    }
}
