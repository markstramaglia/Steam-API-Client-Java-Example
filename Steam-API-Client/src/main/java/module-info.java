module com.example.steamapiclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.steamapiclient to javafx.fxml;
    exports com.example.steamapiclient;
}