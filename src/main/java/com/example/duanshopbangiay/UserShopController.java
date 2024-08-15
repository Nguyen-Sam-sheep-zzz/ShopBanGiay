package com.example.duanshopbangiay;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserShopController implements Initializable {
    @FXML
    private VBox chosenNikeCard;

    @FXML
    private Label nikeNameLable;

    @FXML
    private Label nikePriceLable;

    @FXML
    private ImageView nikeImg;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    private List<Product> products = new ArrayList<>();

    private Image image;

    private MyListener myListener;


    private List<Product> getData() {
        List<Product> products = new ArrayList<>();
        Product product;

        product = new Product();
        product.setId(10);
        product.setName("Jordan1");
        product.setPrice(20);
        product.setQuantity(20);
        product.setColor("6A7324");
        product.setImagePath("/img/Green-removebg-preview.png");
        products.add(product);

        product = new Product();
        product.setId(10);
        product.setName("Jordan1");
        product.setPrice(20);
        product.setQuantity(20);
        product.setColor("6A7324");
        product.setImagePath("C:\\Users\\SAM\\IdeaProjects\\DuAnShopBanGiay\\src\\main\\resources\\img\\Green-removebg-preview.png");
        products.add(product);

        product = new Product();
        product.setId(10);
        product.setName("Jordan1");
        product.setPrice(20);
        product.setQuantity(20);
        product.setColor("A7745B");
        product.setImagePath("C:\\Users\\SAM\\IdeaProjects\\DuAnShopBanGiay\\src\\main\\resources\\img\\Red-removebg-preview.png");
        products.add(product);

        product = new Product();
        product.setId(10);
        product.setName("Jordan1");
        product.setPrice(20);
        product.setQuantity(20);
        product.setColor("F16C31");
        product.setImagePath("C:\\Users\\SAM\\IdeaProjects\\DuAnShopBanGiay\\src\\main\\resources\\img\\Pink-removebg-preview.png");
        products.add(product);

        product = new Product();
        product.setId(10);
        product.setName("Jordan1");
        product.setPrice(20);
        product.setQuantity(20);
        product.setColor("291D36");
        product.setImagePath("C:\\Users\\SAM\\IdeaProjects\\DuAnShopBanGiay\\src\\main\\resources\\img\\YellowOriginal-removebg-preview.png");
        products.add(product);

        product = new Product();
        product.setId(10);
        product.setName("Jordan1");
        product.setPrice(20);
        product.setQuantity(20);
        product.setColor("22371D");
        product.setImagePath("C:\\Users\\SAM\\IdeaProjects\\DuAnShopBanGiay\\src\\main\\resources\\img\\MidN-removebg-preview.png");
        products.add(product);

        product = new Product();
        product.setId(10);
        product.setName("Jordan1");
        product.setPrice(20);
        product.setQuantity(20);
        product.setColor("FB5D03");
        product.setImagePath("C:\\Users\\SAM\\IdeaProjects\\DuAnShopBanGiay\\src\\main\\resources\\img\\Orange-removebg-preview.png");
        products.add(product);

        return products;
    }

    private void setChosenProduct(Product product) {
        nikeNameLable.setText(product.getName());
        nikePriceLable.setText(UserShopApplication.CURRENCY + product.getPrice());
        nikeImg.setImage(image);
        image = new Image(getClass().getResourceAsStream(product.getImagePath()));
        chosenNikeCard.setStyle("-fx-background-color: #" + product.getColor() + ";\n" +
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
        int row = 0;
        try {
            for (int i = 0; i < products.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/views/Item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ItemController itemController = fxmlLoader.getController();
                itemController.setData(products.get(i), myListener);

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

