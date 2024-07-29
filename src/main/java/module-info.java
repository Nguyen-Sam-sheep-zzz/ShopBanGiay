module com.example.duanshopbangiay {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.duanshopbangiay to javafx.fxml;
    exports com.example.duanshopbangiay;
}