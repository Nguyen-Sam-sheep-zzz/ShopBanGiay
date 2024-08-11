package com.example.duanshopbangiay;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class OrderListController {

    @FXML
    private ListView<Order> orderListView;
    @FXML
    private VBox orderContainer;
    @FXML
    private Button refreshButton;
    private List<Order> orders;

    @FXML
    public void initialize() {
        ObservableList<Order> orders = FXCollections.observableArrayList(
                new Order("1", "Tâm", "2024-08-01", 100.0, "Completed", Arrays.asList(
                        new Product("Gucci", 19.99, 2),
                        new Product("Nike", 18.99, 3))),
                new Order("2", "Sâm", "2024-08-02", 150.5, "Pending", Arrays.asList(
                        new Product("LV", 19.99, 2),
                        new Product("adidas", 18.99, 3))),
                new Order("3", "Khánh", "2024-08-03", 200.0, "Shipped", Arrays.asList(
                        new Product("Gucci", 19.99, 2),
                        new Product("Nike", 18.99, 3))),

                new Order("4", "ChiAnh", "2024-08-03", 200.0, "Shipped", Arrays.asList(
                        new Product("Gucci", 19.99, 2),
                        new Product("Nike", 18.99, 3))),

                new Order("5", "Duy", "2024-08-03", 200.0, "Shipped", Arrays.asList(
                        new Product("Gucci", 19.99, 2),
                        new Product("Nike", 18.99, 3))));


        orderListView.setItems(orders);
        orderListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
            @Override
            public ListCell<Order> call(ListView<Order> listView) {
                return new OrderListCell();
            }
        });
    }

    public void refreshOrderList(ActionEvent actionEvent) {

    }

    private static class OrderListCell extends ListCell<Order> {
        @Override
        protected void updateItem(Order item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("order_item.fxml"));
                try {
                    setGraphic(loader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                OrderItemController controller = loader.getController();
                controller.setOrder(item);
            }
        }
    }

    private HBox createOrderItem(Order order) {
        // Tạo một HBox để hiển thị thông tin đơn hàng và các nút hành động
        HBox orderBox = new HBox(10);
        Label orderIdLabel = new Label(order.getOrderId());
        Label customerNameLabel = new Label(order.getCustomerName());
        Label orderDateLabel = new Label(order.getOrderDate().toString());
        Label totalAmountLabel = new Label(String.valueOf(order.getTotalAmount()));
        Label statusLabel = new Label(order.getStatus());

        Button paidButton = new Button("Đã thanh toán");
        Button cancelButton = new Button("Hủy đơn");
        paidButton.setOnAction(event -> {
            order.setStatus("Paid");
            updateOrderInDatabase(order);
            updateOrderList();
        });

        cancelButton.setOnAction(event -> {
            order.setStatus("Canceled");
            updateOrderInDatabase(order);
            updateOrderList();
        });
        orderBox.getChildren().addAll(orderIdLabel, customerNameLabel, orderDateLabel, totalAmountLabel, statusLabel, paidButton, cancelButton);
        return orderBox;
    }


    private void updateOrderList() {
    }


    private List<Order> fetchOrdersFromDatabase() {
        // Thực hiện logic để nạp danh sách đơn hàng từ cơ sở dữ liệu
        // Đây chỉ là một ví dụ, bạn cần thay thế bằng mã thực tế
        return Database.getOrders();
    }

    private void updateOrderInDatabase(Order order) {
        // Thực hiện logic để cập nhật trạng thái đơn hàng trong cơ sở dữ liệu
        // Đây chỉ là một ví dụ, bạn cần thay thế bằng mã thực tế
        Database.updateOrder(order);
    }
}




