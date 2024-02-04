module com.example.chatjava {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.chatjava to javafx.fxml;
    exports com.example.chatjava;
}