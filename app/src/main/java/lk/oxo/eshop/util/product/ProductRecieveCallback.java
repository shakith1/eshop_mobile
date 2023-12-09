package lk.oxo.eshop.util.product;

import java.util.List;

import lk.oxo.eshop.model.Product;

public interface ProductRecieveCallback {
    void onRecieved(List<Product> productList);
}
