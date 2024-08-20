package com.example.duanshopbangiay;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Order {
    private final StringProperty customerName;
    private final SimpleIntegerProperty idProduct;
    private final StringProperty productName;
    private final StringProperty imagePath;
    private final SimpleDoubleProperty price;
    private final SimpleIntegerProperty quantity;
    private final StringProperty color;
    private final StringProperty size;
    private final SimpleDoubleProperty totalAmount;
    private final StringProperty status;

    public Order(String customerName, int idProduct, String productName, String imagePath, double price, int quantity, String color, String size, double totalAmount, String status) {
        this.customerName = new SimpleStringProperty(customerName);
        this.idProduct = new SimpleIntegerProperty(idProduct);
        this.productName = new SimpleStringProperty(productName);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.color = new SimpleStringProperty(color);
        this.size = new SimpleStringProperty(size);
        this.totalAmount = new SimpleDoubleProperty(totalAmount);
        this.status = new SimpleStringProperty(status);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public int getIdProduct() {
        return idProduct.get();
    }

    public void setIdProduct(int idProduct) {
        this.idProduct.set(idProduct);
    }

    public SimpleIntegerProperty idProductProperty() {
        return idProduct;
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public String getImagePath() {
        return imagePath.get();
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color.set(color);
    }

    public StringProperty colorProperty() {
        return color;
    }

    public String getSize() {
        return size.get();
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public SimpleDoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }
}
