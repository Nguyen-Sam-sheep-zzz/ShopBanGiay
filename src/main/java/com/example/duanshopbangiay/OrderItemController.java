package com.example.duanshopbangiay;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class OrderItemController {
    @FXML
    private Label orderIdLable;
    @FXML
    private Label customerNameLable;
    @FXML
    private Label orderDateLable;
    @FXML
    private Label statusLable;
    @FXML
    private Label totalPriceLable;
    @FXML
    private Label statusLabel;
    @FXML
    private VBox productsContainer;
    @FXML
    private Button paidButton;
    @FXML
    private Button cancelButton;
    public void initialize() {
        // Example of adding a product to the VBox
        addProduct("Product 1", 2, 50.0);
        addProduct("Product 2", 1, 100.0);

        // Add action listeners to the buttons if needed
        paidButton.setOnAction(event -> handlePaid());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void addProduct(String s, int i, double v) {
    }

    private void    handlePaid(){
//        Đánh dấu đơn hàng là đã thanh toán
        System.out.println("order marked as paid");
    }
    private void    handleCancel(){
        System.out.println("order marked cancelled");
    }

    public void setOrder (Order order) {
        orderIdLable.setText(order.getOrderid());
        customerNameLable.setText(order.getCustomerName());
        orderDateLable.setText(order.getOrderDate());
        statusLable.setText(order.getStatus());
        statusLable.setText(order.getStatus());
//        Hiển thị sản phẩm
        productsContainer.getChildren().clear();
        for (Product product : order.getProducts()) {
            HBox productBox = new HBox(10);
            Label productName = new Label(product.getName());
            Label productPrice = new Label(product.getPrice() + "Price");
            Label productQuantity = new Label(product.getQuantity() + "Quantity");
            Region spacer = new Region();

            HBox.getHgrow(  spacer); //            Đẩy các lable về bên trải
            productBox.getChildren().addAll(productName, productPrice, productQuantity,spacer);
            productsContainer.getChildren().add(productBox);
            Label productLabel = new Label(product.getName());
            productBox.getChildren().add(productBox);
        }

        paidButton.setOnAction(event -> {
            order.setStatus("Paid");
            statusLable.setText("Paid");
        });
        cancelButton.setOnAction(event -> {
            order.setStatus("Cancelled");
            statusLable.setText("Cancelled");
        });
    }
}

