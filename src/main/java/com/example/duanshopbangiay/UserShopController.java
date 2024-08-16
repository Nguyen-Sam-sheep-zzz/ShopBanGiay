package com.example.duanshopbangiay;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserShopController implements Initializable {
    @FXML
    private VBox chosenProductCard;

    @FXML
    private ImageView productImg;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productPriceLable;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    private List<Product> products = new ArrayList<>();

    private Image image;

    private MyListener myListener;

    private List<Product> getData() {
        List<Product> products = new ArrayList<>();
        Product product;

        product = new Product();
        product.setId(9);
        product.setName("Jordan 1");
        product.setPrice(300);
        product.setQuantity(20);
        product.setColor("32CD32");
        product.setImagePath("/img/Green-remove-preview.png");
        products.add(product);

        product = new Product();
        product.setId(9);
        product.setName("Jordan 1");
        product.setPrice(300);
        product.setQuantity(20);
        product.setColor("00008B");
        product.setImagePath("/img/MidN-remove-preview.png");
        products.add(product);

        product = new Product();
        product.setId(9);
        product.setName("Jordan 1");
        product.setPrice(300);
        product.setQuantity(20);
        product.setColor("D2691E");
        product.setImagePath("/img/Orange-remove-preview.png");
        products.add(product);

        product = new Product();
        product.setId(9);
        product.setName("Jordan 1");
        product.setPrice(300);
        product.setQuantity(20);
        product.setColor("FF6699");
        product.setImagePath("/img/Pink-remove-preview.png");
        products.add(product);

        product = new Product();
        product.setId(9);
        product.setName("Jordan 1");
        product.setPrice(300);
        product.setQuantity(20);
        product.setColor("B22222");
        product.setImagePath("/img/Red-remove-preview.png");
        products.add(product);

        product = new Product();
        product.setId(9);
        product.setName("Jordan 1");
        product.setPrice(300);
        product.setQuantity(20);
        product.setColor("FFB605");
        product.setImagePath("/img/YellowOriginal-remove-preview.png");
        products.add(product);


        return products;
    }

    private void setChosenProduct(Product product) {
        productNameLabel.setText(product.getName());
        productPriceLable.setText(UserShopApplication.CURRENCY + product.getPrice());
        image = new Image(getClass().getResourceAsStream(product.getImagePath()));
        productImg.setImage(image);
        chosenProductCard.setStyle("-fx-background-color: #" + product.getColor() + ";\n" +
                "    -fx-background-radius: 30;");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        products.addAll(getData());
        if (products.size() > 0) {
            setChosenProduct(products.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(Product product) {
                    setChosenProduct(product);
                }
            };
        }
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < products.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(products.get(i),myListener);

                if (column == 3) {
                    column = 0;
                    row++;

                }

                grid.add(anchorPane, column++, row);
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
