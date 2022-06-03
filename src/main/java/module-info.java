module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jimObjModelImporterJFX;
    requires org.json;
    requires java.net.http;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}