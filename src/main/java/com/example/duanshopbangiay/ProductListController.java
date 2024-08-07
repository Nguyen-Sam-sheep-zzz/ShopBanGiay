package com.example.duanshopbangiay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Optional;

public class ProductListController {

    private Stage stage;
    private Scene scene;

    @FXML
    private ListView<Product> productListView;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private Button addButton;

    private ObservableList<Product> productList;

    @FXML
    public void initialize() {
        productList = FXCollections.observableArrayList(
                new Product("Gucci", 19.99, 10),
                new Product("Nike", 29.99, 5),
                new Product("Dior", 15.49, 20)
        );
        productListView.setItems(productList);
        productListView.setCellFactory(createProductCellFactory());
        addButton.setOnAction(event -> addProduct());
    }

    public static Callback<ListView<Product>, ListCell<Product>> createProductCellFactory() {
        return listView -> new ProductListCell();
    }

    public void switchToDisplayLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(DisplayLoginApplication.class.getResource("DisplayLogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    private static class ProductListCell extends ListCell<Product> {
        private HBox content;
        private Button editButton;
        private Button removeButton;

        public ProductListCell() {
            super();
            content = new HBox(10);
            editButton = new Button("Edit");
            removeButton = new Button("Remove");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS); // Đẩy các nút về bên phải

            content.getChildren().addAll(new Label(), spacer, editButton, removeButton); // Đặt spacer ở giữa

            editButton.setOnAction(event -> {
                Product currentItem = getItem();
                if (currentItem != null) {
                    showEditProductDialog(currentItem);
                }
            });

            removeButton.setOnAction(event -> {
                Product currentItem = getItem();
                if (currentItem != null) {
                    showConfirmationDialog(currentItem);
                }
            });
        }

        private void showEditProductDialog(Product product) {
            // Tạo hộp thoại để chỉnh sửa thông tin sản phẩm
            TextInputDialog dialog = new TextInputDialog(product.getName());
            dialog.setTitle("Edit Product");
            dialog.setHeaderText("Update product information");
            dialog.setContentText("Product's name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                // Nhập giá
                TextInputDialog priceDialog = new TextInputDialog(String.valueOf(product.getPrice()));
                priceDialog.setTitle("Edit Product");
                priceDialog.setHeaderText("Update product price");
                priceDialog.setContentText("Product price:");

                Optional<String> priceResult = priceDialog.showAndWait();
                priceResult.ifPresent(priceText -> {
                    // Nhập số lượng
                    TextInputDialog quantityDialog = new TextInputDialog(String.valueOf(product.getQuantity()));
                    quantityDialog.setTitle("Edit Product");
                    quantityDialog.setHeaderText("Update product quantity");
                    quantityDialog.setContentText("Quantity products:");

                    Optional<String> quantityResult = quantityDialog.showAndWait();
                    quantityResult.ifPresent(quantityText -> {
                        // Hiển thị hộp thoại xác nhận
                        showConfirmationUpdateDialog(product, name, priceText, quantityText);
                    });
                });
            });
        }

        private void showConfirmationUpdateDialog(Product product, String newName, String newPriceText, String newQuantityText) {
            // Kiểm tra xem các trường có bị bỏ trống hay không
            if (newName.isEmpty() || newPriceText.isEmpty() || newQuantityText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Invalid information");
                alert.setContentText("Please fill in complete information for product name, price and quantity.");
                alert.showAndWait();
                return; // Dừng lại nếu có lỗi
            }
            // Kiểm tra giá và số lượng có phải là số hợp lệ hay không
            try {
                double newPrice = Double.parseDouble(newPriceText);
                int newQuantity = Integer.parseInt(newQuantityText);

                // Hiển thị hộp thoại xác nhận
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm product update");
                alert.setHeaderText("Are you sure you want to update the product " + product.getName() + " ?");
                alert.setContentText("Click Ok to confirm, or Cancel to cancel.");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Cập nhật thông tin sản phẩm
                        product.setName(newName);
                        product.setPrice(newPrice);
                        product.setQuantity(newQuantity);
                        getListView().refresh(); // Làm mới danh sách để hiển thị thông tin mới
                    }
                });
            } catch (NumberFormatException e) {
                // Hiển thị thông báo lỗi nếu giá hoặc số lượng không hợp lệ
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Invalid information");
                alert.setContentText("Please fill in complete information for product name, price and quantity.");
                alert.showAndWait();
            }
        }

        private void showConfirmationDialog(Product product) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm product deletion ?");
            alert.setHeaderText("Are you sure you want to delete the product " + product.getName() + " ?");
            alert.setContentText("Click Ok to confirm, or Cancel to cancel.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    getListView().getItems().remove(product);
                }
            });
        }

        @Override
        protected void updateItem(Product item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                String displayText = String.format("%s - Price: %.2f - Quantity: %d",
                        item.getName(), item.getPrice(), item.getQuantity());
                ((Label) content.getChildren().get(0)).setText(displayText); // Cập nhật label với thông tin sản phẩm
                setGraphic(content);
            }
        }
    }

    private void addProduct() {
        String name = nameField.getText();
        String priceText = priceField.getText();
        String quantityText = quantityField.getText();

        // Kiểm tra dữ liệu đầu vào
        if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Information");
            alert.setContentText("Please fill in all fields for product name, price, and quantity.");
            alert.showAndWait();
            return; // Exit if there's an error
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            Product newProduct = new Product(name, price, quantity);
            productList.add(newProduct);  // Thêm sản phẩm vào productList
            // Làm trống các trường nhập liệu
            nameField.clear();
            priceField.clear();
            quantityField.clear();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Information");
            alert.setContentText("Please enter valid numbers for product price and quantity.");
            alert.showAndWait();
        }
    }
}