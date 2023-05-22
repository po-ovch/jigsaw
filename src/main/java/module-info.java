module com.jigsaw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javax.websocket.api;
    requires java.naming;
    requires org.glassfish.tyrus.core;
    requires org.glassfish.tyrus.server;
    requires org.glassfish.tyrus.container.grizzly.server;
    requires org.glassfish.tyrus.client;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jdk.httpserver;
    requires java.net.http;
    requires derby;


    opens com.jigsaw.client to javafx.fxml;
    exports com.jigsaw.client;

    opens com.jigsaw.server to javafx.fxml;
    exports com.jigsaw.server;
    exports com.jigsaw.client.viewControllers;
    opens com.jigsaw.client.viewControllers to javafx.fxml;
    exports com.jigsaw.server.viewControllers;
    opens com.jigsaw.server.viewControllers to javafx.fxml;
    exports com.jigsaw.shared;
    opens com.jigsaw.shared to javafx.fxml;
    exports com.jigsaw.client.webControllers;
    opens com.jigsaw.client.webControllers to javafx.fxml;
    exports com.jigsaw.server.webControllers;
    opens com.jigsaw.server.webControllers to javafx.fxml;
    exports com.jigsaw.shared.messages;
    opens com.jigsaw.shared.messages to javafx.fxml;
    exports com.jigsaw.shared.entities;
    opens com.jigsaw.shared.entities to javafx.fxml;
    exports com.jigsaw.server.db to org.hibernate.orm.core;
}