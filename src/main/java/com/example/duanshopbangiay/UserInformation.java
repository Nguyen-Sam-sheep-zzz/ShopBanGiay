package com.example.duanshopbangiay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;


public class UserInformation {

    @FXML
    private TextField fullNameField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Label updatedInfoLabel;

    @FXML
    private VBox displayProfileSettings;

    @FXML
    private Button backButton;

    @FXML
    private HBox HboxUserInformation;

    @FXML
    private HBox HboxOrderInformation;

    private boolean isEditing = false;

    @FXML
    private void initialize() {
        displayProfileSettings.setVisible(false);
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        setEditableFields(false);
        User currentUser = UserSession.getCurrentUser();
        if (currentUser != null) {
            loadUserInfo(currentUser);
        }
        // Đăng ký sự kiện nhấp chuột cho HBox
        HboxUserInformation.setOnMouseClicked(this::handleHboxUserInformationClick);
        HboxOrderInformation.setOnMouseClicked(this::handleHboxOrderInformationClick);
    }
    private void handleHboxOrderInformationClick(MouseEvent event) {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser != null) {
            try {
                // Tải fxml và tạo scene mới
                FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderList.fxml"));
                Parent root = loader.load();

                // Lấy controller và thiết lập thông tin người dùng
                OrderController controller = loader.getController();
                controller.setCurrentUser(currentUser);

                Stage stage = (Stage) HboxOrderInformation.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Order List");
                stage.show();


            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Unable to navigate to Order List page.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid username.");
        }
    }

    private void handleHboxUserInformationClick(MouseEvent event) {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser != null) {
            showUserDetails(currentUser.getUsername());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid username.");
        }
    }

    private void loadUserInfo(User currentUser) {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid username.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get("users.txt"));
            boolean userFound = false;

            for (String line : lines) {
                String[] userData = line.split(",");
                if (userData.length >= 7 && userData[0].trim().equalsIgnoreCase(currentUser.getUsername().trim())) {
                    fullNameField.setText(userData[1].trim());
                    genderComboBox.setValue(userData[2].trim());
                    phoneNumberField.setText(userData[3].trim());
                    emailField.setText(userData[4].trim());
                    userFound = true;
                    break;
                }
            }

            if (!userFound) {
                showAlert(Alert.AlertType.ERROR, "Error", "User not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load user information.");
        }
    }

    private void updateUserInfo(String fullName, String gender, String email, String phoneNumber) {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null || currentUser.getUsername().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid username.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get("users.txt"));
            boolean userFound = false;

            for (int i = 0; i < lines.size(); i++) {
                String[] userData = lines.get(i).split(",");
                if (userData.length >= 7 && userData[0].trim().equalsIgnoreCase(currentUser.getUsername().trim())) {
                    lines.set(i, String.format("%s,%s,%s,%s,%s,%s,%s",
                            currentUser.getUsername(), fullName, gender, phoneNumber, email, userData[5], userData[6]));
                    userFound = true;
                    break;
                }
            }

            if (!userFound) {
                showAlert(Alert.AlertType.ERROR, "Error", "User not found.");
                return;
            }

            Files.write(Paths.get("users.txt"), lines, StandardOpenOption.TRUNCATE_EXISTING);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Information has been updated.");
        } catch (IOException e) {
            e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Unable to save user information.");
        }
    }

    private void setEditableFields(boolean editable) {
        fullNameField.setEditable(editable);
        genderComboBox.setDisable(!editable);
        emailField.setEditable(editable);
        phoneNumberField.setEditable(editable);
    }

    @FXML
    private void handleBackAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserShop.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Shop");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot return to User Shop page.");
        }
    }

    @FXML
    public void handleEditProfile(ActionEvent actionEvent) {
        isEditing = true;
        setEditableFields(true);
        displayProfileSettings.setVisible(true);
        updatedInfoLabel.setVisible(false);
    }

    @FXML
    public void handleSaveChanges(ActionEvent actionEvent) {
        if (!isEditing) {
            return;
        }

        String fullName = fullNameField.getText().trim();
        String gender = genderComboBox.getValue();
        String email = emailField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();

        if (fullName.isEmpty() || gender.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all information.");
            return;
        }

        if (!isValidEmail(email)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid email.");
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid phone number.");
            return;
        }

        updateUserInfo(fullName, gender, email, phoneNumber);
        setEditableFields(false);
        displayProfileSettings.setVisible(false);
        updatedInfoLabel.setVisible(true);
        isEditing = false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{10}$");
    }

    @FXML
    private void validateEmail() {
        String email = emailField.getText().trim();
        if (!isValidEmail(email)) {
            emailField.setStyle("-fx-border-color: #ff0000;");
        } else {
            emailField.setStyle(null);
        }
    }

    @FXML
    private void validatePhoneNumber() {
        String phoneNumber = phoneNumberField.getText().trim();
        if (!isValidPhoneNumber(phoneNumber)) {
            phoneNumberField.setStyle("-fx-border-color: #ff0000;");
        } else {
            phoneNumberField.setStyle(null);
        }
    }

    public void showUserDetails(String username) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Details");
        alert.setHeaderText("User: " + username);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        try {
            List<String> lines = Files.readAllLines(Paths.get("users.txt"));
            boolean userFound = false;

            for (String line : lines) {
                String[] userData = line.split(",");
                if (userData.length >= 7 && userData[0].trim().equalsIgnoreCase(username.trim())) {
                    gridPane.add(new Label("Full Name:"), 0, 0);
                    gridPane.add(new Label(userData[1].trim()), 1, 0);

                    gridPane.add(new Label("Gender:"), 0, 1);
                    gridPane.add(new Label(userData[2].trim()), 1, 1);

                    gridPane.add(new Label("Phone Number:"), 0, 2);
                    gridPane.add(new Label(userData[3].trim()), 1, 2);

                    gridPane.add(new Label("Email:"), 0, 3);
                    gridPane.add(new Label(userData[4].trim()), 1, 3);

                    userFound = true;
                    break;
                }
            }

            if (!userFound) {
                gridPane.add(new Label("User not found."), 0, 0);
            }

        } catch (IOException e) {
            e.printStackTrace();
            gridPane.add(new Label("Error reading user information."), 0, 0);
        }

        alert.getDialogPane().setContent(gridPane);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }
}
