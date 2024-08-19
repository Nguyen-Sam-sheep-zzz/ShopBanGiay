package com.example.duanshopbangiay;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;

public class CartViewController {
    private Stage stage;
    private Scene scene;

    @FXML
    private TableView<Product> cartTableView;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, ImageView> imageColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, String> sizeColumn;
    @FXML
    private TableColumn<Product, String> colorColumn;
    @FXML
    private TableColumn<Product, Void> actionsColumn;
    @FXML
    private Label totalAmountLabel;

    private ObservableList<Product> cartItems = FXCollections.observableArrayList();

    private Cart cart;


    @FXML
    public void initialize() {
        if (colorColumn != null) {
            colorColumn.setVisible(false);
        }
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        imageColumn.setCellValueFactory(param -> {
            Product product = param.getValue();
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(product.getImagePath())));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            return new SimpleObjectProperty<>(imageView);
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));

        // Cột Màu
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

        // Cột Hành động
        actionsColumn.setCellFactory(new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<Product, Void>() {
                    private final Button deleteButton = new Button("Delete");
                    {
                        deleteButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            // Hiển thị thông báo xác nhận trước khi xóa sản phẩm
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Xác nhận xóa");
                            alert.setHeaderText(null);
                            alert.setContentText("Bạn có chắc chắn muốn xóa sản phẩm " + product.getName() + " khỏi giỏ hàng?");
                            // Xử lý kết quả từ thông báo xác nhận
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    // Nếu người dùng đồng ý, xóa sản phẩm khỏi giỏ hàng
                                    cartItems.remove(product);
                                    if (cart != null) {
                                        cart.removeProduct(product);
                                    }
                                    saveCartToFile();
                                    updateTotalAmount();
                                }
                                // Nếu người dùng không đồng ý, không làm gì cả
                            });
                        });
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : deleteButton);
                        setText(null);
                    }
                };
            }
        });
        loadCartItemsFromFile();
        cartTableView.setItems(cartItems);
        updateTotalAmount();
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        cartItems.setAll(cart.getProducts());
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        double total = cartItems.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
        totalAmountLabel.setText(String.format("$%.2f", total));
    }
    public void loadCartItemsFromFile() {

        String filePath = "Cart.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 7) {
                    int id = Integer.parseInt(details[0]);
                    String name = details[1];
                    double price = Double.parseDouble(details[2]);
                    int quantity = Integer.parseInt(details[3]);
                    String color = details[4];
                    String size = details[5];
                    String imagePath = details[6];

                    Product product = new Product(id, name, price, quantity, color, size, imagePath);
                    cartItems.add(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveCartToFile() {
        String filePath = "Cart.txt";

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            for (Product product : cartItems) {
                pw.printf("%d,%s,%.2f,%d,%s,%s,%s%n",
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getColor(),
                        product.getSize(),
                        product.getImagePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


