package com.example.duanshopbangiay;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageTableCell extends TableCell {
    private final ImageView imageView = new ImageView();

    public ImageTableCell() {
        // Cài đặt kích thước cố định cho ImageView
        imageView.setFitWidth(50); // hoặc kích thước mong muốn
        imageView.setFitHeight(50); // hoặc kích thước mong muốn
        imageView.setPreserveRatio(true);
        setGraphic(imageView);// Đảm bảo tỷ lệ ảnh không bị thay đổi
    }

    protected void updateItem(Image image, boolean empty) {
        super.updateItem(image, empty);
        if (empty || image == null) {
            setGraphic(null);
        } else {
            imageView.setImage(image);
            setGraphic(imageView);
        }
    }
}

