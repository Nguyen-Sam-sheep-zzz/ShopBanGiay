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

    // Admin data (in a real application, this would be loaded from a database)
    Admin admin1 = new Admin("NhanSam", "123" );
    Admin admin2 = new Admin("ChiAnh", "123");
    Admin admin3 = new Admin("VietTam", "123");
    Admin admin4 = new Admin("DucDuy", "123");

    // User data (in a real application, this would be loaded from a database)
    List<User> userList = RegisterController.getUserList();

    public void switchToRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("Register.fxml"));
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
        String usernameText = this.username.getText();
        String passwordText = this.password.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Add admin data to the user list
        userList.add(admin1);
        userList.add(admin2);
        userList.add(admin3);
        userList.add(admin4);

        if (usernameText.isEmpty() || passwordText.isEmpty()) {
            alert.setContentText("Error: Username and Password cannot be empty.");
            alert.show();
            return;
        }

        User user = findUser(usernameText);

        if (user == null || !user.getPassword().equals(passwordText)) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
            return;
        }

        // Save the current user to the session
        UserSession.setCurrentUser(user);

        if (!user.getRole().equals("admin")) {
            alert.setContentText("Login successful! Hello, user " + usernameText);
            alert.show();
            Parent root = FXMLLoader.load(LoginApplication.class.getResource("UserShop.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("User Shop");
            stage.setScene(scene);
            stage.show();
        } else {
            alert.setContentText("Login successful! Hello, admin " + usernameText);
            alert.show();
            Parent root = FXMLLoader.load(LoginApplication.class.getResource("DisplayShop.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Admin Shop");
            stage.setScene(scene);
            stage.show();
        }
    }

    private User findUser(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }
}