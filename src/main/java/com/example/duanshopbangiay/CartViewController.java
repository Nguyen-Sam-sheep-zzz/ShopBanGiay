package com.example.duanshopbangiay;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.event.ActionEvent;

import java.io.*;

public class CartViewController {
    @FXML
    private Button payButton;
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
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

        actionsColumn.setCellFactory(new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<Product, Void>() {
                    private final Button deleteButton = new Button("Delete");
                    {
                        deleteButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirm Delete");
                            alert.setHeaderText(null);
                            alert.setContentText("Are you sure you want to delete " + product.getName() + " from the cart?");
                            alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    cartItems.remove(product);
                                    if (cart != null) {
                                        cart.removeProduct(product);
                                        saveCartToFile();
                                    }
                                    updateTotalAmount();
                                }
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
        payButton.setOnAction(this::handlePayNow);
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        cartItems.setAll(cart.getProducts());
        updateTotalAmount();
    }

    public void updateTotalAmount() {
        double total = cartItems.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
        totalAmountLabel.setText(String.format("$%.2f", total));
    }

    public void loadCartItemsFromFile() {
        cartItems.clear();
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

    private void saveOrderToFile(String username, ObservableList<Product> products, double totalAmount) {
        String filePath = "allOrder.txt";
        int totalProducts = products.size(); // Tổng số sản phẩm trong đơn hàng

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            // Xây dựng chuỗi chứa thông tin chi tiết về sản phẩm
            StringBuilder productDetails = new StringBuilder();
            for (Product product : products) {
                if (productDetails.length() > 0) {
                    productDetails.append(";");
                }
                productDetails.append(String.format("%d:%s:%s:%.2f:%d:%s:%s",
                        product.getId(),
                        product.getName(),
                        product.getImagePath(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getColor(),
                        product.getSize()));
            }
            pw.printf("%s,%d,%s,%.2f,%s%n",
                    username,                       // Tên người dùng
                    totalProducts,                  // Tổng số sản phẩm
                    productDetails.toString(),      // Chi tiết sản phẩm
                    totalAmount,                    // Tổng giá
                    "Waiting for payment"           // Trạng thái đơn hàng
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handlePayNow(ActionEvent event) {
        double totalAmount = cartItems.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Payment");
        alert.setHeaderText(null);
        alert.setContentText("The total amount for your order is $" + String.format("%.2f", totalAmount) + ". Do you want to proceed with the payment?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String currentUser = getCurrentUser();
                saveOrderToFile(currentUser, cartItems, totalAmount);

                cartItems.clear();
                updateTotalAmount();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Payment Successful");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Your order has been placed successfully.");
                successAlert.show();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        });
    }

    private String getCurrentUser() {
        User currentUser = UserSession.getCurrentUser();
        return (currentUser != null) ? currentUser.getUsername() : "Unknown User";
    }
}
