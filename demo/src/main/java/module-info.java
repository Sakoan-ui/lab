module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}