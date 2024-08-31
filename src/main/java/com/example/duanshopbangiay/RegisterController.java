package com.example.duanshopbangiay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField realNameField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    private static List<User> userList = new ArrayList<>();

    // Static block to load users when the class is loaded
    static {
        loadUsers();
    }

    @FXML
    private void initialize() {
        // Initialize the gender ComboBox with options
        genderComboBox.getItems().addAll("Male", "Female", "Other");
    }

    @FXML
    private void handleRegisterButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String realName = realNameField.getText();
        String gender = genderComboBox.getValue();
        String phone = phoneField.getText();
        String email = emailField.getText();

        // Validate các trường
        if (username.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Username is required");
            return;
        }
        if (!validateUsername(username)) {
            showAlert(AlertType.ERROR, "Form Error!", "Username must be at least 5 characters long and contain only letters and numbers");
            return;
        }
        if (password.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Password is required");
            return;
        }
        if (confirmPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Confirm Password is required");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Form Error!", "Passwords do not match");
            return;
        }
        if (realName.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Real Name is required");
            return;
        }
        if (gender == null) {
            showAlert(AlertType.ERROR, "Form Error!", "Gender is required");
            return;
        }
        if (phone.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Phone number is required");
            return;
        }
        if (!validatePhoneNumber(phone)) {
            showAlert(AlertType.ERROR, "Form Error!", "Invalid phone number");
            return;
        }
        if (email.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Email is required");
            return;
        }
        if (!validateEmail(email)) {
            showAlert(AlertType.ERROR, "Form Error!", "Invalid email address");
            return;
        }

        // Check if username already exists
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                showAlert(AlertType.ERROR, "Registration Error", "Username already exists. Please choose another one.");
                return;
            }
        }

        // Tạo User mới với đầy đủ thông tin và role mặc định là "user"
        User newUser = new User(username, realName, gender, phone, email, password, "user");
        userList.add(newUser);

        // Save the new user immediately
        saveUsers();

        showAlert(AlertType.INFORMATION, "Registration Successful!", "Welcome " + realName + ", you can now log in with your new account.");
        clearFields();
    }

    // Kiểm tra định dạng của username
    private boolean validateUsername(String username) {
        return username.matches("^[a-zA-Z0-9]{5,}$");
    }

    // Kiểm tra định dạng của số điện thoại
    private boolean validatePhoneNumber(String phone) {
        return phone.matches("^\\d{10,11}$");
    }

    // Kiểm tra định dạng của email
    private boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        realNameField.clear();
        genderComboBox.setValue(null);
        phoneField.clear();
        emailField.clear();
    }

    public void switchToDisplayLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("DisplayLogin.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Phương thức lưu thông tin người dùng vào file
    private static void saveUsers() {
        Path path = Paths.get("users.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            for (User user : userList) {
                // Lưu theo thứ tự: username, realName, gender, phoneNumber, email, password, role
                writer.write(user.getUsername() + "," + user.getRealName() + "," + user.getGender() + "," + user.getPhoneNumber() + "," + user.getEmail() + "," + user.getPassword() + "," + user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức tải thông tin người dùng từ file
    private static void loadUsers() {
        Path path = Paths.get("users.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    // Tạo đối tượng User theo thứ tự: username, realName, gender, phoneNumber, email, password, role
                    String username = parts[0];
                    String realName = parts[1];
                    String gender = parts[2];
                    String phoneNumber = parts[3];
                    String email = parts[4];
                    String password = parts[5];
                    String role = parts[6];

                    userList.add(new User(username, realName, gender, phoneNumber, email, password, role));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getUserList() {
        return userList;
    }
}