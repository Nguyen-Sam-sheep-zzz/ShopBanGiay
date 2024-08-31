package com.example.duanshopbangiay;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDisplay {
    private  SimpleStringProperty orderId;
    private  SimpleStringProperty customerName;
    private  SimpleStringProperty totalProducts;
    private  ObservableList<Product> products;
    private  SimpleStringProperty totalAmount;
    private  SimpleStringProperty status;
    private  SimpleStringProperty productImages;
    private  SimpleObjectProperty<LocalDateTime> orderTime;// New attribute

    public OrderDisplay(){

    }

    public OrderDisplay(int orderId, String customerName, List<Product> products, double totalAmount, String status, LocalDateTime orderTime) {
        this.orderId = new SimpleStringProperty(String.valueOf(orderId));
        this.customerName = new SimpleStringProperty(customerName);
        this.totalProducts = new SimpleStringProperty(String.valueOf(products.size()));
        this.products = FXCollections.observableArrayList(products);
        this.totalAmount = new SimpleStringProperty(String.valueOf(totalAmount));
        this.status = new SimpleStringProperty(status);
        this.orderTime = new SimpleObjectProperty<>(orderTime); // Initialize orderTime

        // Combine image paths
        StringBuilder images = new StringBuilder();
        for (Product product : products) {
            if (images.length() > 0) {
                images.append(", ");
            }
            images.append(product.getImagePath());
        }
        this.productImages = new SimpleStringProperty(images.toString());
    }

    public String getOrderId() {
        return orderId.get();
    }

    public SimpleStringProperty orderIdProperty() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    public String getTotalProducts() {
        return totalProducts.get();
    }

    public SimpleStringProperty totalProductsProperty() {
        return totalProducts;
    }

    public void setOrderId(String orderId) {
        this.orderId.set(orderId);
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public void setTotalProducts(String totalProducts) {
        this.totalProducts.set(totalProducts);
    }

    public void setProducts(ObservableList<Product> products) {
        this.products = products;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public void setProductImages(String productImages) {
        this.productImages.set(productImages);
    }

    public ObservableList<Product> getProducts() {
        return products;
    }

    public String getTotalAmount() {
        return totalAmount.get();
    }

    public SimpleStringProperty totalAmountProperty() {
        return totalAmount;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public String getProductImages() {
        return productImages.get();
    }

    public SimpleStringProperty productImagesProperty() {
        return productImages;
    }

    public LocalDateTime getOrderTime() {
        return orderTime.get();
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime.set(orderTime);
    }

    public SimpleObjectProperty<LocalDateTime> orderTimeProperty() {
        return orderTime;
    }
}