package com.jigsaw.client;

import com.jigsaw.client.viewControllers.ViewManager;
import com.jigsaw.client.webControllers.WebsocketClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;

public class JigsawApplication extends Application {
    public static Stage startStage;

    @Override
    public void start(Stage stage) throws IOException {
        SharedComponents.viewManager = new ViewManager(stage);
        SharedComponents.server = new WebsocketClient();
    }

    public static void main(String[] args) {
        launch();
    }
}