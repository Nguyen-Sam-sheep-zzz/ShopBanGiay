package com.example.duanshopbangiay;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String color;
    private String size; // Thêm thuộc tính size
    private String imagePath;

    Product() {

    }
    public Product(int id, String name, double price, int quantity, String color, String size, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.color = color;
        this.size = size; // Cập nhật thuộc tính size
        this.imagePath = imagePath;
    }

    // Getters và setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size; // Getter cho size
    }

    public void setSize(String size) {
        this.size = size; // Setter cho size
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image getImage() {
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        return imageStream != null ? new Image(imageStream) : null;
    }
}
