package com.example.duanshopbangiay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.MouseEvent;


public class ItemController {
    @FXML
    private Label nameLable;

    @FXML
    private Label priceLable;

    @FXML
    private ImageView img;

    @FXML
    private void click(MouseEvent mouseEvent) {
        myListener.onClickListener(product);
    }

    private Product product;

    private MyListener myListener;

    public void setData(Product product, MyListener myListener) {
        this.product = product;
        this.myListener = myListener;
        nameLable.setText(product.getName());
        priceLable.setText(UserShopApplication.CURRENCY + product.getPrice());

        // Sử dụng đường dẫn tương đối
        Image image = new Image(getClass().getResourceAsStream(product.getImagePath()));
        img.setImage(image);
    }


}
