package com.example.duanshopbangiay;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Order {
    private SimpleIntegerProperty id; // Thêm thuộc tính id
    private SimpleStringProperty customerName;
    private SimpleListProperty<Product> products;
    private SimpleDoubleProperty totalAmount;
    private SimpleStringProperty status;

    public Order(int id, String customerName, List<Product> products, double totalAmount, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.customerName = new SimpleStringProperty(customerName);
        this.products = new SimpleListProperty<>(FXCollections.observableArrayList(products));
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.status = new SimpleStringProperty(status);
    }

    // Getter và setter cho id
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    // Getter và setter cho customerName
    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    // Getter và setter cho products
    public ObservableList<Product> getProducts() {
        return products.get();
    }

    public void setProducts(List<Product> products) {
        this.products.setAll(products);
    }

    // Getter và setter cho totalAmount
    public double getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    // Getter và setter cho status
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
