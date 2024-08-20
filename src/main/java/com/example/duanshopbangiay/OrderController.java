package com.example.duanshopbangiay;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OrderController {

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, String> customerNameColumn;

    @FXML
    private TableColumn<Order, Integer> idProductColumn;

    @FXML
    private TableColumn<Order, String> productNameColumn;

    @FXML
    private TableColumn<Order, String> imageColumn;

    @FXML
    private TableColumn<Order, Double> priceColumn;

    @FXML
    private TableColumn<Order, Integer> quantityColumn;

    @FXML
    private TableColumn<Order, String> colorColumn;

    @FXML
    private TableColumn<Order, String> sizeColumn;

    @FXML
    private TableColumn<Order, Double> totalAmountColumn;

    @FXML
    private TableColumn<Order, String> orderStatusColumn;

    @FXML
    private TableColumn<Order, Void> actionColumn;

    @FXML
    private Button backButton;

    private ObservableList<Order> orders = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colorColumn.setVisible(false);
        // Initialize table columns
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        idProductColumn.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        // Sử dụng ImageView trong cột hình ảnh
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        imageColumn.setCellFactory(column -> new TableCell<Order, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    try {
                        // Sử dụng đường dẫn tương đối từ thư mục resources
                        InputStream imageStream = getClass().getResourceAsStream(imagePath);
                        if (imageStream != null) {
                            Image image = new Image(imageStream);
                            imageView.setImage(image);
                            imageView.setFitHeight(50);  // Giới hạn chiều cao
                            imageView.setFitWidth(50);   // Giới hạn chiều rộng
                            imageView.setPreserveRatio(true); // Giữ tỷ lệ ảnh
                            setGraphic(imageView);
                        } else {
                            // Nếu không tìm thấy ảnh, hiển thị một ảnh mặc định hoặc thông báo lỗi
                            Image defaultImage = new Image("/img/default.png"); // Đảm bảo có một ảnh mặc định trong thư mục resources
                            imageView.setImage(defaultImage);
                            imageView.setFitHeight(50);
                            imageView.setFitWidth(50);
                            imageView.setPreserveRatio(true);
                            setGraphic(imageView);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        setGraphic(null);
                    }
                }
            }
        });


        // Các cột khác
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Create action buttons
        actionColumn.setCellFactory(new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
            @Override
            public TableCell<Order, Void> call(TableColumn<Order, Void> param) {
                return new TableCell<>() {
                    private final Button payButton = new Button("Paid");
                    private final Button cancelButton = new Button("Cancel Order");

                    {
                        payButton.setOnAction(event -> {
                            Order order = getTableView().getItems().get(getIndex());
                            if ("Waiting for payment".equals(order.getStatus())) {
                                order.setStatus("Paid");
                                updateOrderInFile(order);
                            }
                        });

                        cancelButton.setOnAction(event -> {
                            Order order = getTableView().getItems().get(getIndex());
                            if ("Waiting for payment".equals(order.getStatus())) {
                                order.setStatus("Cancelled");
                                updateOrderInFile(order);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            if ("Waiting for payment".equals(getTableView().getItems().get(getIndex()).getStatus())) {
                                setGraphic(new HBox(10, payButton, cancelButton));
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });

        // Load orders from file
        orderLoader();
    }
    private void orderLoader() {
        String filePath = "allOrder.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String customerName = data[0];
                    String productDetails = data[1];
                    double totalAmount = Double.parseDouble(data[2]);
                    String status = data[3];

                    // Xử lý thông tin sản phẩm
                    String[] products = productDetails.split(";");
                    for (String product : products) {
                        String[] productData = product.split(":");
                        if (productData.length == 7) {
                            int idProduct = Integer.parseInt(productData[0]);
                            String productName = productData[1];
                            String imagePath = productData[2];
                            double price = Double.parseDouble(productData[3]);
                            int quantity = Integer.parseInt(productData[4]);
                            String color = productData[5];
                            String size = productData[6];

                            // Tạo hoặc cập nhật đơn hàng
                            boolean found = false;
                            for (Order existingOrder : orders) {
                                if (existingOrder.getIdProduct() == idProduct && existingOrder.getSize().equals(size)) {
                                    existingOrder.setQuantity(existingOrder.getQuantity() + quantity);
                                    existingOrder.setTotalAmount(existingOrder.getTotalAmount() + price * quantity);
                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {
                                Order order = new Order(customerName, idProduct, productName, imagePath, price, quantity, color, size, price * quantity, status);
                                orders.add(order);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set items to TableView
        orderTable.setItems(orders);
    }

    private void updateOrderInFile(Order updatedOrder) {
        String filePath = "allOrder.txt";
        List<String> fileContent = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String customerName = data[0];
                    String productDetails = data[1];
                    double totalAmount = Double.parseDouble(data[2]);
                    String status = data[3];

                    if (customerName.equals(updatedOrder.getCustomerName()) && productDetails.contains(String.valueOf(updatedOrder.getIdProduct()))) {
                        // Cập nhật dòng có trạng thái mới
                        String newProductDetails = updateProductDetails(productDetails, updatedOrder);
                        line = customerName + "," + newProductDetails + "," + updatedOrder.getTotalAmount() + "," + updatedOrder.getStatus();
                    }
                }
                fileContent.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cập nhật lại TableView sau khi thay đổi
        orderTable.refresh();
    }

    private String updateProductDetails(String productDetails, Order updatedOrder) {
        StringBuilder newDetails = new StringBuilder();
        String[] products = productDetails.split(";");
        for (String product : products) {
            String[] productData = product.split(":");
            if (Integer.parseInt(productData[0]) == updatedOrder.getIdProduct() && productData[6].equals(updatedOrder.getSize())) {
                newDetails.append(String.format("%d:%s:%s:%.2f:%d:%s:%s",
                        updatedOrder.getIdProduct(),
                        updatedOrder.getProductName(),
                        updatedOrder.getImagePath(),
                        updatedOrder.getPrice(),
                        updatedOrder.getQuantity(),
                        updatedOrder.getColor(),
                        updatedOrder.getSize()));
            } else {
                newDetails.append(product);
            }
            newDetails.append(";");
        }
        if (newDetails.length() > 0) {
            newDetails.setLength(newDetails.length() - 1); // Loại bỏ dấu chấm phẩy cuối cùng
        }
        return newDetails.toString();
    }

    @FXML
    private void switchToDisplayShop() {

    }

}

