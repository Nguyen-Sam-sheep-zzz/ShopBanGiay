package com.example.duanshopbangiay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

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

    @FXML
    public void initialize() {
        ObservableList<Product> products = FXCollections.observableArrayList(
                new Product("Gucci", 19.99, 10),
                new Product("Nike", 29.99, 5),
                new Product("Dior", 15.49, 20)
        );

        productListView.setItems(products);
        productListView.setCellFactory(createProductCellFactory());

        addButton.setOnAction(event -> addProduct());
    }

    public static Callback<ListView<Product>, ListCell<Product>> createProductCellFactory() {
        return listView -> new ProductListCell();
    }

    public void switchToDisplayLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(HelloApplication.class.getResource("DisplayLogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
                System.out.println("Editing: " + currentItem.getName());
                // Thêm logic edit thực tế ở đây
            });

            removeButton.setOnAction(event -> {
                getListView().getItems().remove(getItem());
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
            System.out.println("Please fill all fields");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            Product newProduct = new Product(name, price, quantity);
            productListView.getItems().add(newProduct);

            // Làm trống các trường nhập liệu
            nameField.clear();
            priceField.clear();
            quantityField.clear();
        } catch (NumberFormatException e) {
            System.out.println("Invalid price or quantity");
        }
    }
}
