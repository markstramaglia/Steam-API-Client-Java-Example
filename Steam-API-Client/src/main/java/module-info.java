module com.example.steamapiclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;


    opens com.example.steamapiclient to javafx.fxml;
    exports com.example.steamapiclient;
}