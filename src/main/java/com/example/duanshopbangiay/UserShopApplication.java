package com.example.duanshopbangiay;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class UserShopApplication extends Application {
    public static final String CURRENCY = "$";
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(LoginApplication.class.getResource("UserShop.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Nike Display ");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}