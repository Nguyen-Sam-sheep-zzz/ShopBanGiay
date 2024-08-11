package com.example.duanshopbangiay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {
    private static List<Order> orders = new ArrayList<>();

    static {
        // Add some dummy data
        orders.add(new Order("1", "Customer A", new Date(), 100.0, "Pending"));
        orders.add(new Order("2", "Customer B", new Date(), 150.0, "Pending"));
    }

    public static List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public static void updateOrder(Order order) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId().equals(order.getOrderId())) {
                orders.set(i, order);
                break;
            }
        }
    }
}
