module com.jigsaw.jigsaw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.jigsaw.jigsaw.client to javafx.fxml;
    exports com.jigsaw.jigsaw.client;

    opens com.jigsaw.jigsaw.server to javafx.fxml;
    exports com.jigsaw.jigsaw.server;
    exports com.jigsaw.jigsaw;
    opens com.jigsaw.jigsaw to javafx.fxml;
}