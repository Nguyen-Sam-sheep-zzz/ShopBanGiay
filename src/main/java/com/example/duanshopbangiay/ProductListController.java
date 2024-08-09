package com.example.duanshopbangiay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Optional;
public class ProductListController {

    private Stage stage;
    private Scene scene;
    private int nextId = 1; // Biến để tự động tăng ID cho sản phẩm mới

    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, Integer> idColumn; // Cột ID
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, Void> actionsColumn; // Cột hành động

    @FXML
    private TextField idField; // Trường ID
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;

    private ObservableList<Product> productList;

    @FXML
    public void initialize() {
        productList = FXCollections.observableArrayList(
                new Product(nextId++, "Gucci", 19.99, 10),
                new Product(nextId++, "Nike", 29.99, 5),
                new Product(nextId++, "Dior", 15.49, 20)
        );

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // Thiết lập cột ID
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Thiết lập cột Actions để chứa các nút Edit và Remove
        actionsColumn.setCellFactory(getActionsCellFactory());
        productTableView.setItems(productList);
    }

    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> getActionsCellFactory() {
        return col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button removeButton = new Button("Remove");
            private final HBox actionButtons = new HBox(editButton, removeButton);

            {
                editButton.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    editProduct(product);
                });

                removeButton.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    removeProduct(product);
                });

                actionButtons.setSpacing(10);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButtons);
                }
            }
        };
    }

    private void editProduct(Product product) {
        showEditProductDialog(product);
    }

    private void removeProduct(Product product) {
        showConfirmationDialog(product);
    }

    public void switchToDisplayLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("DisplayLogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToOrderList(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("OrderList.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("OrderList");
        stage.setScene(scene);
        stage.show();
    }
    private void showEditProductDialog(Product product) {
        while (true) {
            // Tạo một dialog mới
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Product");

            // Tạo các trường nhập liệu
            TextField idField = new TextField(String.valueOf(product.getId()));
            TextField nameField = new TextField(product.getName());
            TextField priceField = new TextField(String.valueOf(product.getPrice()));
            TextField quantityField = new TextField(String.valueOf(product.getQuantity()));

            // Tạo một lưới (GridPane) để bố trí các trường nhập liệu
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(new Label("Product ID:"), 0, 0);
            grid.add(idField, 1, 0);
            grid.add(new Label("Product Name:"), 0, 1);
            grid.add(nameField, 1, 1);
            grid.add(new Label("Product Price:"), 0, 2);
            grid.add(priceField, 1, 2);
            grid.add(new Label("Product Quantity:"), 0, 3);
            grid.add(quantityField, 1, 3);

            dialog.getDialogPane().setContent(grid);

            // Thêm các nút OK và Cancel
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Hiển thị dialog và kiểm tra kết quả
            Optional<ButtonType> result = dialog.showAndWait();

            // Kiểm tra nếu người dùng nhấn Cancel
            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                break; // Nếu người dùng nhấn Cancel, thoát khỏi vòng lặp
            }

            // Xử lý sự kiện khi nhấn OK
            String idText = idField.getText();
            String name = nameField.getText();
            String priceText = priceField.getText();
            String quantityText = quantityField.getText();

            // Kiểm tra trường nhập liệu có bị bỏ trống không
            if (idText.isEmpty() || name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                showError("Invalid information", "Please fill in complete information for product ID, name, price, and quantity.");
                continue; // Tiếp tục vòng lặp để yêu cầu người dùng nhập lại
            }

            try {
                int newId = Integer.parseInt(idText);
                double newPrice = Double.parseDouble(priceText);
                int newQuantity = Integer.parseInt(quantityText);

                // Kiểm tra trùng ID, ngoại trừ sản phẩm hiện tại đang chỉnh sửa
                boolean isDuplicate = false;
                for (Product p : productList) {
                    if (p.getId() == newId && p != product) {
                        isDuplicate = true;
                        break; // Nếu phát hiện ID trùng, thoát khỏi vòng lặp kiểm tra
                    }
                }

                if (isDuplicate) {
                    showError("Duplicate ID", "Product ID already exists. Please enter a different ID.");
                    continue; // Nếu ID bị trùng, tiếp tục vòng lặp mà không cập nhật sản phẩm
                }

                // Nếu không có lỗi, cập nhật thông tin sản phẩm
                product.setId(newId);
                product.setName(name);
                product.setPrice(newPrice);
                product.setQuantity(newQuantity);
                productTableView.refresh(); // Cập nhật bảng
                break; // Thoát khỏi vòng lặp nếu dữ liệu hợp lệ
            } catch (NumberFormatException e) {
                showError("Invalid information", "Please enter valid numbers for product ID, price, and quantity.");
                // Nếu có lỗi, tiếp tục vòng lặp để yêu cầu người dùng nhập lại
            }
        }
    }

    private void showConfirmationDialog(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm product deletion ?");
        alert.setHeaderText("Are you sure you want to delete the product " + product.getName() + " ?");
        alert.setContentText("Click Ok to confirm, or Cancel to cancel.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                productList.remove(product);
            }
        });
    }

    @FXML
    private void addProduct() {
        String idText = idField.getText();
        String name = nameField.getText();
        String priceText = priceField.getText();
        String quantityText = quantityField.getText();

        if (idText.isEmpty() || name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            showError("Error", "Please fill in all fields for product ID, name, price, and quantity.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            // Kiểm tra trùng ID
            for (Product product : productList) {
                if (product.getId() == id) {
                    showError("Duplicate ID", "Product ID already exists. Please enter a different ID.");
                    return;
                }
            }

            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            Product newProduct = new Product(id, name, price, quantity);
            productList.add(newProduct);

            nameField.clear();
            priceField.clear();
            quantityField.clear();
            idField.clear();
        } catch (NumberFormatException e) {
            if (!idText.matches("\\d+")) {
                showError("Invalid ID", "Please enter a valid numeric ID.");
            } else if (!priceText.matches("\\d+(\\.\\d+)?")) {
                showError("Invalid Price", "Please enter a valid numeric price.");
            } else if (!quantityText.matches("\\d+")) {
                showError("Invalid Quantity", "Please enter a valid numeric quantity.");
            } else {
                showError("Error", "Please enter valid numbers for product ID, price, and quantity.");
            }
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
