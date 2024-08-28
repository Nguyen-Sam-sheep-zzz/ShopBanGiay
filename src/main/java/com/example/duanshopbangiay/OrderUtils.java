package com.example.duanshopbangiay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class OrderUtils {

    private static final String ORDER_ID_FILE = "lastOrderId.txt";

    // Lấy ID đơn hàng tiếp theo
    public static int getNextOrderId() {
        int lastOrderId = readLastOrderId();
        return lastOrderId + 1;
    }

    // Cập nhật ID đơn hàng cuối cùng vào tệp
    public static void updateLastOrderId(int newOrderId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_ID_FILE))) {
            writer.write(String.valueOf(newOrderId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Đọc ID đơn hàng cuối cùng từ tệp
    private static int readLastOrderId() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_ID_FILE))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                return Integer.parseInt(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // Nếu không tìm thấy tệp hoặc không đọc được giá trị, trả về 0
    }
}
