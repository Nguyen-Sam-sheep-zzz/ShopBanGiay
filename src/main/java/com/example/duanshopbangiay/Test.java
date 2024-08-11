package com.example.duanshopbangiay;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Objects;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DisplayShop.fxml"));
        primaryStage.setTitle("Product List");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class OrderListController {

        @javafx.fxml.FXML
        private ListView<Order> orderListView;

        @FXML
        public void initialize() {
            ObservableList<Order> orders = FXCollections.observableArrayList(
                    new Order("1", "Tâm", "2024-08-01", 100.0, "Completed"),
                    new Order("2", "Sâm", "2024-08-02", 150.5, "Pending"),
                    new Order("3", "Khánh", "2024-08-03", 200.0, "Shipped")
            );

            orderListView.setItems(orders);
            orderListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
                @Override
                public ListCell<Order> call(ListView<Order> listView) {
                    return new OrderListCell();
                }
            });
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
    }
}
