module com.jigsaw.jigsaw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
//    requires javax.websocket.api;
    requires java.naming;
    requires org.glassfish.tyrus.core;
    requires org.glassfish.tyrus.server;
    requires org.glassfish.tyrus.container.grizzly.server;
    requires org.glassfish.tyrus.client;
    requires com.google.gson;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


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
    exports com.jigsaw.jigsaw.shared;
    opens com.jigsaw.jigsaw.shared to javafx.fxml;
    exports com.jigsaw.jigsaw.client.web;
    opens com.jigsaw.jigsaw.client.web to javafx.fxml;
    exports com.jigsaw.jigsaw.server.web;
    opens com.jigsaw.jigsaw.server.web to javafx.fxml;
    exports com.jigsaw.jigsaw.shared.messages;
    opens com.jigsaw.jigsaw.shared.messages to javafx.fxml;
    exports com.jigsaw.jigsaw.shared.entites;
    opens com.jigsaw.jigsaw.shared.entites to javafx.fxml;
}