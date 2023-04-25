module com.jigsaw.jigsaw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javax.websocket.api;
    requires org.glassfish.tyrus.core;
    requires org.glassfish.tyrus.server;
    requires org.glassfish.tyrus.container.grizzly.server;
    requires org.glassfish.tyrus.client;
    requires com.google.gson;



    opens com.jigsaw.jigsaw.client to javafx.fxml;
    exports com.jigsaw.jigsaw.client;

    opens com.jigsaw.jigsaw.server to javafx.fxml;
    exports com.jigsaw.jigsaw.server;
    exports com.jigsaw.jigsaw;
    opens com.jigsaw.jigsaw to javafx.fxml;
    exports com.jigsaw.jigsaw.client.viewControllers;
    opens com.jigsaw.jigsaw.client.viewControllers to javafx.fxml;
    exports com.jigsaw.jigsaw.server.viewControllers;
    opens com.jigsaw.jigsaw.server.viewControllers to javafx.fxml;
    exports com.jigsaw.jigsaw.endpoint;
    opens com.jigsaw.jigsaw.endpoint to javafx.fxml;
    exports com.jigsaw.jigsaw.client.web;
    opens com.jigsaw.jigsaw.client.web to javafx.fxml;
    exports com.jigsaw.jigsaw.server.web;
    opens com.jigsaw.jigsaw.server.web to javafx.fxml;
    exports com.jigsaw.jigsaw.endpoint.messages;
    opens com.jigsaw.jigsaw.endpoint.messages to javafx.fxml;
    exports com.jigsaw.jigsaw.endpoint.shared;
    opens com.jigsaw.jigsaw.endpoint.shared to javafx.fxml;
}