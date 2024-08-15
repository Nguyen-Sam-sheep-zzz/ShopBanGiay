package com.example.duanshopbangiay;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.MouseEvent;

public class ItemController {

    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLable;

    @FXML
//    public void click (MouseEvent mouseEvent) {
//        myListener.onClickListener(product);
//    }

    private Product product;

    private MyListener myListener;

    public void setData(Product product, MyListener myListener) {
        this.product = product;
        this.myListener = myListener;
        nameLabel.setText(product.getName());
        priceLable.setText(UserShopApplication.CURRENCY + product.getPrice());
        Image image = new Image(getClass().getResourceAsStream(product.getImagePath()));
        img.setImage(image);
    }

    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        myListener.onClickListener(product);
    }
}