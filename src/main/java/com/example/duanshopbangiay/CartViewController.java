package com.example.duanshopbangiay;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    private ObservableList<Product> productList;

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
    private void saveOrderToFile(OrderDisplay orderDisplay) {
        String filePath = "allOrder.txt";
        int orderId = Integer.parseInt(orderDisplay.getOrderId()); // Lấy ID đơn hàng từ đối tượng Order
        String customerName = orderDisplay.getCustomerName(); // Lấy tên khách hàng
        ObservableList<Product> products = orderDisplay.getProducts(); // Lấy danh sách sản phẩm
        double totalAmount = Double.parseDouble(orderDisplay.getTotalAmount()); // Lấy tổng giá
        String status = orderDisplay.getStatus(); // Lấy trạng thái đơn hàng

        int totalProducts = products.size(); // Tổng số sản phẩm trong đơn hàng

        // Lấy thời gian hiện tại và định dạng nó
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

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
            pw.printf("%d,%s,%d,%s,%.2f,%s,%s%n",
                    orderId,                        // ID đơn hàng
                    customerName,                   // Tên người dùng
                    totalProducts,                  // Tổng số sản phẩm
                    productDetails.toString(),      // Chi tiết sản phẩm
                    totalAmount,                    // Tổng giá
                    status,                         // Trạng thái đơn hàng
                    formattedDateTime               // Thời gian đặt hàng
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handlePayNow(ActionEvent event) {

        // Kiểm tra nếu giỏ hàng trống
        if (cartItems.isEmpty()) {
            // Hiển thị thông báo cảnh báo nếu giỏ hàng trống
            Alert emptyCartAlert = new Alert(Alert.AlertType.WARNING);
            emptyCartAlert.setTitle("Empty Cart");
            emptyCartAlert.setHeaderText(null);
            emptyCartAlert.setContentText("Your cart is empty. Please add products before proceeding to payment.");
            emptyCartAlert.show();
            return; // Ngừng thực hiện nếu giỏ hàng trống
        }

        // Tính tổng số tiền của đơn hàng từ danh sách sản phẩm trong giỏ hàng
        double totalAmount = cartItems.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();

        // Hiển thị hộp thoại xác nhận thanh toán
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Payment Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("The total amount for your order is $" + String.format("%.2f", totalAmount) + ".Do you want to continue payment?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Lấy thông tin người dùng hiện tại
                String currentUser = getCurrentUser();

                // Lấy thời gian hiện tại
                LocalDateTime orderTime = LocalDateTime.now();

                // Tạo đối tượng Order với các thông tin cần thiết
                int orderId = OrderUtils.getNextOrderId(); // Lấy ID đơn hàng tiếp theo
                OrderUtils.updateLastOrderId(orderId); // Cập nhật ID đơn hàng cuối cùng

                OrderDisplay orderDisplay = new OrderDisplay(
                        orderId,
                        currentUser,
                        new ArrayList<>(cartItems), // Chuyển đổi ObservableList thành ArrayList
                        totalAmount,
                        "Waiting for payment", // Trạng thái đơn hàng
                        orderTime // Thêm thời gian đơn hàng
                );

                // Lưu đơn hàng vào tệp
                saveOrderToFile(orderDisplay);

                updateProductStock();

                // Xóa giỏ hàng và cập nhật tổng số tiền
                cartItems.clear();
                updateTotalAmount();

                clearCartFile();

                // Hiển thị thông báo thành công
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Payment Successful");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Your order has been placed successfully.");
                successAlert.show();

                // Đóng cửa sổ hiện tại
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
                UserShopController userShopController = new UserShopController();
                userShopController.refresh();
            }
        });
    }



    private void updateProductStock() {
        // Đọc danh sách sản phẩm từ file products.txt
        List<Product> productsInStock = new ArrayList<>();
        File file = new File("products.txt");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        double price = Double.parseDouble(parts[2]);
                        int quantity = Integer.parseInt(parts[3]);
                        String color = parts[4];
                        String size = parts[5];
                        String imagePath = parts[6];

                        Product product = new Product(id, name, price, quantity, color, size, imagePath);
                        productsInStock.add(product);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Cập nhật số lượng sản phẩm trong kho dựa trên sản phẩm đã mua
        for (Product cartProduct : cartItems) {
            for (Product stockProduct : productsInStock) {
                if (cartProduct.getId() == stockProduct.getId()) {
                    // Trừ số lượng sản phẩm trong kho dựa trên số lượng đã mua
                    int updatedQuantity = stockProduct.getQuantity() - cartProduct.getQuantity();
                    stockProduct.setQuantity(updatedQuantity);
                    break;
                }
            }
        }

        // Lưu lại danh sách sản phẩm đã cập nhật số lượng vào file products.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
            for (Product product : productsInStock) {
                writer.write(product.getId() + "," +
                        product.getName() + "," +
                        product.getPrice() + "," +
                        product.getQuantity() + "," +
                        product.getColor() + "," +
                        product.getSize() + "," +
                        product.getImagePath());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void clearCartFile() {
        try {
            FileWriter fileWriter = new FileWriter("Cart.txt", false);
            fileWriter.write(""); // Ghi chuỗi rỗng vào tệp để xóa toàn bộ nội dung
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentUser() {
        User currentUser = UserSession.getCurrentUser();
        return (currentUser != null) ? currentUser.getUsername() : "Unknown User";
    }
}
