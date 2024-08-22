package com.example.duanshopbangiay;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    @FXML
    private TableView<OrderDisplay> orderTable;
    @FXML
    private TableColumn<OrderDisplay, String> orderIdColumn;
    @FXML
    private TableColumn<OrderDisplay, String> customerNameColumn;
    @FXML
    private TableColumn<OrderDisplay, String> totalProductsColumn;
    @FXML
    private TableColumn<OrderDisplay, String> idProductColumn;
    @FXML
    private TableColumn<OrderDisplay, String> productNameColumn;
    @FXML
    private TableColumn<OrderDisplay, String> imageColumn;
    @FXML
    private TableColumn<OrderDisplay, String> priceColumn;
    @FXML
    private TableColumn<OrderDisplay, String> quantityColumn;
    @FXML
    private TableColumn<OrderDisplay, String> colorColumn;
    @FXML
    private TableColumn<OrderDisplay, String> sizeColumn;
    @FXML
    private TableColumn<OrderDisplay, String> totalAmountColumn;
    @FXML
    private TableColumn<OrderDisplay, String> orderStatusColumn;

    private ObservableList<OrderDisplay> orderList;

    @FXML
    public void initialize() {
        orderList = FXCollections.observableArrayList();

        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        totalProductsColumn.setCellValueFactory(new PropertyValueFactory<>("totalProducts"));

        idProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getProducts().stream()
                        .map(product -> String.valueOf(product.getId()))
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
        ));

        productNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getProducts().stream()
                        .map(Product::getName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
        ));

        imageColumn.setCellValueFactory(cellData -> cellData.getValue().productImagesProperty());
        imageColumn.setCellFactory(column -> new TableCell<OrderDisplay, String>() {
            @Override
            protected void updateItem(String imagePaths, boolean empty) {
                super.updateItem(imagePaths, empty);
                if (empty || imagePaths == null || imagePaths.isEmpty()) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox();
                    String[] paths = imagePaths.split(", ");
                    for (String path : paths) {
                        InputStream imageStream = getClass().getResourceAsStream(path);
                        if (imageStream == null) {
                            imageStream = getClass().getResourceAsStream("/img/placeholder.png");
                        }
                        if (imageStream != null) {
                            try {
                                Image image = new Image(imageStream);
                                ImageView imageView = new ImageView(image);
                                imageView.setFitWidth(50);
                                imageView.setFitHeight(50);
                                hbox.getChildren().add(imageView);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    setGraphic(hbox);
                }
            }
        });

        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getProducts().stream()
                        .map(Product::getPrice)
                        .map(String::valueOf)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
        ));

        quantityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getProducts().stream()
                        .map(Product::getQuantity)
                        .map(String::valueOf)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
        ));

        colorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getProducts().stream()
                        .map(Product::getColor)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
        ));

        sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getProducts().stream()
                        .map(Product::getSize)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
        ));

        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        idProductColumn.setVisible(false);
        productNameColumn.setVisible(false);
        priceColumn.setVisible(false);
        quantityColumn.setVisible(false);
        colorColumn.setVisible(false);
        sizeColumn.setVisible(false);

        loadOrdersFromFile("allOrder.txt");
        orderTable.setItems(orderList);
    }

    private void loadOrdersFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) { // Đảm bảo số phần tử đúng
                    int orderId = Integer.parseInt(parts[0].trim());
                    String customerName = parts[1].trim();
                    int totalProducts = Integer.parseInt(parts[2].trim());
                    String[] productDetails = parts[3].split(";");
                    List<Product> products = new ArrayList<>();
                    for (String detail : productDetails) {
                        String[] productInfo = detail.split(":");
                        if (productInfo.length == 7) { // Đảm bảo số phần tử đúng
                            int id = Integer.parseInt(productInfo[0].trim());
                            String name = productInfo[1].trim();
                            String imagePath = productInfo[2].trim();
                            double price = Double.parseDouble(productInfo[3].trim());
                            int quantity = Integer.parseInt(productInfo[4].trim());
                            String color = productInfo[5].trim();
                            String size = productInfo[6].trim();

                            Product product = new Product(id, name, price, quantity, color, size, imagePath);
                            products.add(product);
                        }
                    }
                    double totalAmount = Double.parseDouble(parts[4].trim());
                    String status = parts[5].trim();

                    OrderDisplay orderDisplay = new OrderDisplay(orderId, customerName, products, totalAmount, status);
                    orderList.add(orderDisplay);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToDisplayShop(ActionEvent event) throws IOException {
        // Chuyển đến giao diện cửa hàng (cần được triển khai)
    }

    public static class OrderDisplay {
        private final SimpleStringProperty orderId;
        private final SimpleStringProperty customerName;
        private final SimpleStringProperty totalProducts;
        private final ObservableList<Product> products;
        private final SimpleStringProperty totalAmount;
        private final SimpleStringProperty status;
        private final SimpleStringProperty productImages;

        public OrderDisplay(int orderId, String customerName, List<Product> products, double totalAmount, String status) {
            this.orderId = new SimpleStringProperty(String.valueOf(orderId));
            this.customerName = new SimpleStringProperty(customerName);
            this.totalProducts = new SimpleStringProperty(String.valueOf(products.size()));
            this.products = FXCollections.observableArrayList(products);
            this.totalAmount = new SimpleStringProperty(String.valueOf(totalAmount));
            this.status = new SimpleStringProperty(status);

            // Combine image paths
            StringBuilder images = new StringBuilder();
            for (Product product : products) {
                if (images.length() > 0) {
                    images.append(", ");
                }
                images.append(product.getImagePath());
            }
            this.productImages = new SimpleStringProperty(images.toString());
        }

        public String getOrderId() {
            return orderId.get();
        }

        public SimpleStringProperty orderIdProperty() {
            return orderId;
        }

        public String getCustomerName() {
            return customerName.get();
        }

        public SimpleStringProperty customerNameProperty() {
            return customerName;
        }

        public String getTotalProducts() {
            return totalProducts.get();
        }

        public SimpleStringProperty totalProductsProperty() {
            return totalProducts;
        }

        public ObservableList<Product> getProducts() {
            return products;
        }

        public String getTotalAmount() {
            return totalAmount.get();
        }

        public SimpleStringProperty totalAmountProperty() {
            return totalAmount;
        }

        public String getStatus() {
            return status.get();
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }

        public String getProductImages() {
            return productImages.get();
        }

        public SimpleStringProperty productImagesProperty() {
            return productImages;
        }
    }
}
