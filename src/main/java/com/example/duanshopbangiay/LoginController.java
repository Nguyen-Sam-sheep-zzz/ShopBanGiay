package com.example.duanshopbangiay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    private Stage stage;

    private Scene scene;

    private Parent root;

    User user = new User("ChiAnh", "123456");
    User user1 = new User("VietTam", "123456");
    User user2 = new User("DucDuy", "123456");
    Admin admin = new Admin("NhanSam", "123456");

    User[] users = {user, user2, user1, admin,};

//    public void switchToDisplayLogin(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(HelloApplication.class.getResource("DisplayLogin.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//    public void switchToDisplayShop(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(HelloApplication.class.getResource("DisplayShop.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

    public void switchToDisplayShop(ActionEvent event) throws IOException {
        String username = this.username.getText();
        String password = this.password.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if(username.isEmpty() || password.isEmpty()) {
            alert.setContentText("Error Username and Password cannot be empty.");
            alert.show();
        }
        if (findUser(username).getUsername().equals(username) && findUser(username).getPassword().equals(password)) {
            if (!findUser(username).getRole().equals("admin")) {
                alert.setContentText("Login successful hello user " + username);
                alert.show();
            } else {
                alert.setContentText("Login successful hello admin " + username);
                alert.show();
            }
            Parent root = FXMLLoader.load(HelloApplication.class.getResource("DisplayShop.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            alert.setContentText("Login failed ,Incorrect username or password");
            alert.show();
        }
    }

    public boolean isExist(String username) {
        for (User value : users) {
            if (value.getUsername().equals(username))
                return true;
        }
        return false;
    }

    public User findUser(String username) {
        for (User value : users) {
            if (value.getUsername().equals(username))
                return value;
        }
        return null;
    }
}