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
import java.util.List;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    private Stage stage;

    private Scene scene;

    private Parent root;

    Admin admin1 = new Admin("NhanSam", "123");
    Admin admin2 = new Admin("ChiAnh", "123");
    Admin admin3 = new Admin("VietTam", "123");
    Admin admin4 = new Admin("DucDuy", "123");

    List<User> userList = RegisterController.getUserList();

    //    public void switchToDisplayLogin(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(HelloApplication.class.getResource("DisplayLogin.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
    public void switchToRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(DisplayLoginApplication.class.getResource("Register.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void switchToDisplayShop(ActionEvent event) throws IOException {
        String username = this.username.getText();
        String password = this.password.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        List<User> userList = RegisterController.getUserList();
        userList.add(admin1);
        userList.add(admin2);
        userList.add(admin3);
        userList.add(admin4);

        if (username.isEmpty() || password.isEmpty()) {
            alert.setContentText("Error Username and Password cannot be empty.");
            alert.show();
            return;
        }
        boolean isValidUser = userList.stream().anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
        if (!isValidUser) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
        }

        if (findUser(username).getUsername().equals(username) && findUser(username).getPassword().equals(password)) {
            if (!findUser(username).getRole().equals("admin")) {
                alert.setContentText("Login successful hello user " + username);
                alert.show();
            } else {
                alert.setContentText("Login successful hello admin " + username);
                alert.show();
            }
            Parent root = FXMLLoader.load(DisplayLoginApplication.class.getResource("DisplayShop.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Nike");
            stage.setScene(scene);
            stage.show();
        }
    }
    //    public boolean isExist(String username) {
//        for (User value : users) {
//            if (value.getUsername().equals(username))
//                return true;
//        }
//        return false;
//    }
    public User findUser(String username) {
        for (User value : userList) {
            if (value.getUsername().equals(username))
                return value;
        }
        return null;
    }
}