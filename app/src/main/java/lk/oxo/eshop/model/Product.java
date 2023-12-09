package lk.oxo.eshop.model;

import android.net.Uri;

import java.util.List;

public class Product {
    private String title;
    private String description;
    private double price;
    private long quantity;
    private List<Uri> images;

    public Product() {
    }

    public Product(String title, double price) {
        this.title = title;
        this.price = price;
    }

    public Product(String title, String description, double price, int quantity, List<Uri> images) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public List<Uri> getImages() {
        return images;
    }

    public void setImages(List<Uri> images) {
        this.images = images;
    }
}
