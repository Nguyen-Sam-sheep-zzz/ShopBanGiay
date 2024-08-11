package com.example.duanshopbangiay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private String Orderid;
    private String CustomerName;
    private String orderDate;
    private double totalAmount;
    private String status;
    private List<Product> products;

    public Order(String orderid, String customerName, String orderDate, double totalAmount, String status) {
        this.Orderid = orderid;
        this.CustomerName = customerName;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.products = products;

    }

    public Order(String number, String tâm, String date, double totalAmount, String completed, List<Product> list) {
    }

    public Order(String number, String customerA, Date date, double totalAmount, String pending) {

    }

    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public String getOrderid() {
        return Orderid;
    }
    public void setOrderid(String orderid) {
        this.Orderid = orderid;
    }
    public String getCustomerName() {
        return CustomerName;
    }
    public void setCustomerName(String customerName) {
        this.CustomerName = customerName;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return Orderid;
    }
    public void setOrderId(String orderId) {
        this.Orderid = orderId;
    }
}
