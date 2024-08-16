package com.example.duanshopbangiay;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String color; // Thêm thuộc tính màu
    private String imagePath;

    public Product() {

    }

    public Product(int id, String name, double price, int quantity, String color, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.color = color; // Cập nhật thuộc tính màu
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
        return color; // Getter cho màu
    }

    public void setColor(String color) {
        this.color = color; // Setter cho màu
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image getImage() {
        // Sử dụng getClass().getResource để lấy đường dẫn hình ảnh
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        return imageStream != null ? new Image(imageStream) : null;
    }
}
