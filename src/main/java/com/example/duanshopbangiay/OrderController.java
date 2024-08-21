package com.example.duanshopbangiay;

import javafx.beans.property.SimpleObjectProperty;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    @FXML
    private TableView<OrderDisplay> orderTable;
    @FXML
    private TableColumn<OrderDisplay, String> customerNameColumn;
    @FXML
    private TableColumn<OrderDisplay, String> totalProducts;
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
    @FXML
    private TableColumn<OrderDisplay, String> actionColumn;

    private ObservableList<OrderDisplay> orderList;
    @FXML
    public void initialize() {
        orderList = FXCollections.observableArrayList();
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        totalProducts.setCellValueFactory(new PropertyValueFactory<>("totalProducts"));

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

        // Update the imageColumn cell factory
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

        productNameColumn.setVisible(false);
        priceColumn.setVisible(false);
        quantityColumn.setVisible(false);


        loadOrdersFromFile("allOrder.txt");
        orderTable.setItems(orderList);
    }

    private void loadOrdersFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2) {
                    String customerName = parts[0];
                    int totalProducts = Integer.parseInt(parts[1]);
                    List<Product> products = new ArrayList<>();
                    double totalAmount = Double.parseDouble(parts[parts.length - 2]);
                    String status = parts[parts.length - 1];

                    String[] productDetails = parts[2].split(";");
                    for (String detail : productDetails) {
                        String[] productInfo = detail.split(":");
                        int id = Integer.parseInt(productInfo[0]);
                        String name = productInfo[1];
                        String imagePath = productInfo[2];
                        double price = Double.parseDouble(productInfo[3]);
                        int quantity = Integer.parseInt(productInfo[4]);
                        String color = productInfo[5];
                        String size = productInfo[6];

                        Product product = new Product(id, name, price, quantity, color, size, imagePath);
                        products.add(product);
                    }

                    OrderDisplay orderDisplay = new OrderDisplay(customerName, products, totalAmount, status);
                    orderList.add(orderDisplay);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToDisplayShop(ActionEvent event) throws IOException {

    }
    public static class OrderDisplay {
        private final SimpleStringProperty customerName;
        private final SimpleStringProperty totalProducts;
        private final ObservableList<Product> products;
        private final SimpleStringProperty totalAmount;
        private final SimpleStringProperty status;
        private final SimpleStringProperty productImages; // Add this property

        public OrderDisplay(String customerName, List<Product> products, double totalAmount, String status) {
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
