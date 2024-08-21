package com.example.duanshopbangiay;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Order {
    private SimpleStringProperty customerName;
    private SimpleListProperty<Product> products;
    private SimpleDoubleProperty totalAmount;
    private SimpleStringProperty status;

    public Order(String customerName, List<Product> products, double totalAmount, String status) {
        this.customerName = new SimpleStringProperty(customerName);
        this.products = new SimpleListProperty<>(FXCollections.observableArrayList(products));
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.status = new SimpleStringProperty(status);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public ObservableList<Product> getProducts() {
        return products.get();
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public String getStatus() {
        return status.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public void setProducts(List<Product> products) {
        this.products.setAll(products);
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
