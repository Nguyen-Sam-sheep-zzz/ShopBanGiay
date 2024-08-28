package com.example.duanshopbangiay;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;

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
    private TableColumn<OrderDisplay, String> orderTimeColumn;
    @FXML
    private TableColumn<OrderDisplay, String> orderStatusColumn;
    @FXML
    private TableColumn<OrderDisplay, String> actionColumn;

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
        imageColumn.setCellFactory(column -> new TableCell<>() {
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
        orderTimeColumn.setCellValueFactory(cellData -> {
            LocalDateTime orderTime = cellData.getValue().getOrderTime();
            String formattedTime = orderTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return new SimpleStringProperty(formattedTime);
        });
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button paidButton = new Button("Paid");
            private final Button canceledButton = new Button("Canceled");

            {
                // Set up buttons' actions
                paidButton.setOnAction(e -> handleAction("Paid"));
                canceledButton.setOnAction(e -> handleAction("Canceled"));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                OrderDisplay order = getTableRow().getItem();
                if ("waiting for payment".equalsIgnoreCase(order.getStatus())) {
                    HBox hbox = new HBox(10, paidButton, canceledButton);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                } else {
                    setGraphic(null);
                }
            }

            private void handleAction(String action) {
                OrderDisplay order = getTableRow().getItem();
                if (order != null) {
                    String newStatus = "Paid".equals(action) ? "paid" : "canceled";
                    order.setStatus(newStatus);
                    updateOrderStatus(order.getOrderId(), newStatus); // orderId kiểu int
                    getTableView().refresh();
                }
            }

            private void updateOrderStatus(String orderId, String newStatus) {
                try {
                    Path filePath = Paths.get("allOrder.txt");
                    List<String> lines = Files.readAllLines(filePath);
                    for (int i = 0; i < lines.size(); i++) {
                        String line = lines.get(i);
                        if (line.contains(orderId)) { // Sử dụng orderId kiểu String
                            // Cập nhật trạng thái trong dòng
                            String updatedLine = line.replace("waiting for payment", newStatus);
                            lines.set(i, updatedLine);
                            Files.write(filePath, lines);
                            saveOrdersToFile();
                            return;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        idProductColumn.setVisible(false);
        productNameColumn.setVisible(false);
        priceColumn.setVisible(false);
        quantityColumn.setVisible(false);
        colorColumn.setVisible(false);
        sizeColumn.setVisible(false);

        orderTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra nếu là nhấp chuột đơn
                OrderDisplay selectedOrder = orderTable.getSelectionModel().getSelectedItem();
                if (selectedOrder != null) {
                    showOrderDetails(selectedOrder);
                }
            }
        });

        loadOrdersFromFile();
        orderTable.setItems(orderList);
    }
    private void loadOrdersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("allOrder.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    continue;
                }
                try {
                    // Đọc thông tin đơn hàng
                    int orderId = Integer.parseInt(parts[0].trim());
                    String customerName = parts[1].trim();
                    String totalProducts = parts[2].trim();
                    // Xử lý sản phẩm
                    String[] productStrings = parts[3].split(";"); // Chỉ sử dụng ";" làm dấu phân cách
                    List<Product> products = new ArrayList<>();
                    for (String productString : productStrings) {
                        String[] productParts = productString.split(":");
                        if (productParts.length < 7) {
                            continue;
                        }
                        int id = Integer.parseInt(productParts[0]);
                        String name = productParts[1];
                        String imagePath = productParts[2];
                        double price = Double.parseDouble(productParts[3]);
                        int quantity = Integer.parseInt(productParts[4]);
                        String color = productParts[5];
                        String size = productParts[6];
                        products.add(new Product(id, name, price, quantity, color, size, imagePath));
                    }

                    double totalAmount = Double.parseDouble(parts[4].trim());
                    String status = parts[5].trim();
                    LocalDateTime orderTime = LocalDateTime.parse(parts[6].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    OrderDisplay order = new OrderDisplay(orderId, customerName, products, totalAmount, status, orderTime);
                    orderList.add(order);
                } catch (NumberFormatException | DateTimeParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveOrdersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("allOrder.txt"))) {
            for (OrderDisplay order : orderList) {
                StringBuilder line = new StringBuilder();

                // Ghi ID đơn hàng
                line.append(order.getOrderId()).append(",");
                // Ghi tên khách hàng
                line.append(order.getCustomerName()).append(",");
                // Ghi tổng số sản phẩm
                line.append(order.getProducts().size()).append(",");

                // Ghi danh sách sản phẩm
                for (Product product : order.getProducts()) {
                    line.append(product.getId()).append(":")
                            .append(product.getName()).append(":")
                            .append(product.getImagePath()).append(":")
                            .append(product.getPrice()).append(":")
                            .append(product.getQuantity()).append(":")
                            .append(product.getColor()).append(":")
                            .append(product.getSize()).append("; ");
                }

                // Loại bỏ ký tự phân cách cuối cùng nếu có
                if (line.length() > 0 && line.charAt(line.length() - 2) == ';') {
                    line.setLength(line.length() - 2); // Loại bỏ ký tự '; '
                }

                // Ghi tổng số tiền
                line.append(",").append(order.getTotalAmount()).append(",");
                // Ghi trạng thái đơn hàng
                line.append(order.getStatus()).append(",");
                // Ghi thời gian đơn hàng
                line.append(order.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                // Ghi dòng mới
                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showOrderDetails(OrderDisplay order) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Order Details");
        alert.setHeaderText("Order ID: " + order.getOrderId());

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.add(new Label("Customer name:"), 0, 0);
        gridPane.add(new Label(order.getCustomerName()), 1, 0);

        gridPane.add(new Label("Total products:"), 0, 1);
        gridPane.add(new Label(order.getTotalProducts()), 1, 1);

        gridPane.add(new Label("Product ID:"), 0, 2);
        gridPane.add(new Label(order.getProducts().stream()
                .map(product -> String.valueOf(product.getId()))
                .reduce((a, b) -> a + ", " + b)
                .orElse("")), 1, 2);

        gridPane.add(new Label("Full product name:"), 0, 3);
        gridPane.add(new Label(order.getProducts().stream()
                .map(Product::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("")), 1, 3);

        gridPane.add(new Label("Image:"), 0, 4);
        HBox imageBox = new HBox();
        for (Product product : order.getProducts()) {
            String imagePath = product.getImagePath();
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageBox.getChildren().add(imageView);
        }
        gridPane.add(imageBox, 1, 4);

        gridPane.add(new Label("Quantity:"), 0, 5);
        gridPane.add(new Label(order.getProducts().stream()
                .map(Product::getQuantity)
                .map(String::valueOf)
                .reduce((a, b) -> a + ", " + b)
                .orElse("")), 1, 5);

        gridPane.add(new Label("Size:"), 0, 6);
        gridPane.add(new Label(order.getProducts().stream()
                .map(Product::getSize)
                .reduce((a, b) -> a + ", " + b)
                .orElse("")), 1, 6);

        gridPane.add(new Label("Total amount:"), 0, 7);
        gridPane.add(new Label(order.getTotalAmount()), 1, 7);

        gridPane.add(new Label("Status:"), 0, 8);
        gridPane.add(new Label(order.getStatus()), 1, 8);

        alert.getDialogPane().setContent(gridPane);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }

    public void switchToDisplayShop(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("DisplayShop.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("DisplayShop");
        stage.setScene(scene);
        stage.show();
    }
}
