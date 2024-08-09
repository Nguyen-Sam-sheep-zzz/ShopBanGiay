package com.example.duanshopbangiay;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class OrderController {

    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, String> customerNameColumn;
    @FXML
    private TableColumn<Order, String> productNameColumn;
    @FXML
    private TableColumn<Order, Integer> quantityColumn;
    @FXML
    private TableColumn<Order, Double> totalAmountColumn;
    @FXML
    private TableColumn<Order, String> orderStatusColumn;
    @FXML
    private TableColumn<Order, Void> actionColumn;

    private ObservableList<Order> orderList;

    @FXML
    private void initialize() {
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

        orderList = FXCollections.observableArrayList(fetchOrdersFromDatabase());
        orderTable.setItems(orderList);

        // Thêm nút hành động (Đã thanh toán / Hủy đơn)
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button payButton = new Button("Paid");
            private final Button cancelButton = new Button("Cancel order");

            {
                payButton.setOnAction(event -> handlePayOrder(getTableView().getItems().get(getIndex())));
                cancelButton.setOnAction(event -> handleCancelOrder(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Order order = getTableView().getItems().get(getIndex());
                    if ("Waiting for payment".equals(order.getOrderStatus())) {
                        setGraphic(new HBox(10, payButton, cancelButton));
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private ArrayList<Order> fetchOrdersFromDatabase() {
        // Giả lập dữ liệu từ "database"
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order("Chí Anh", "Adidas", 2, 2000000, "Waiting for payment"));
        orders.add(new Order("Viết Tâm", "Nike", 1, 1500000, "Waiting for payment"));
        orders.add(new Order("Đức Duy", "Puma", 3, 3000000, "Order cancelled"));
        orders.add(new Order("Nhân Sâm", "Converse", 3, 6000000, "Paid"));
        return orders;
    }

    private void handlePayOrder(Order order) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Payment Confirmation");
        alert.setHeaderText("Are you sure you want to change the order status to Paid?");
        alert.setContentText("Click OK to continue, Cancel to cancel");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            order.setOrderStatus("Paid");
            orderTable.refresh();
        }
    }

    private void handleCancelOrder(Order order) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm cancellation");
        alert.setHeaderText("Are you sure you want to change the order status to Canceled?");
        alert.setContentText("Click OK to continue, Cancel to cancel");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            order.setOrderStatus("Cancel order");
            orderTable.refresh();
        }
    }

    public void switchToDisplayShop(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(LoginApplication.class.getResource("DisplayShop.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }
}
