package com.example.duanshopbangiay;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private static final ProductService instance = new ProductService();
    private List<Product> products = new ArrayList<>();

    private ProductService() {
        // Singleton pattern
    }

    public static ProductService getInstance() {
        return instance;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
