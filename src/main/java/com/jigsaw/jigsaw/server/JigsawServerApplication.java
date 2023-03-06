package com.jigsaw.jigsaw.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JigsawServerApplication extends Application {
    public static Stage startStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JigsawServerApplication.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 426, 306);
        stage.setResizable(false);
        stage.setTitle("Jigsaw server");
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> onClose());
        stage.show();

        startStage = stage;
    }

    public static void main(String[] args) {
        launch();
    }

    private static void onClose() {
        if (StartController.server != null) {
            try {
                StartController.server.close();
            } catch (IOException ignored) {
                System.out.println("meow");
            }
        }
        if (StartController.gameSetupThread != null) {
            StartController.gameSetupThread.interrupt();
        }
        for (var clientSocket : StartController.clientSockets) {
            clientSocket.close();
        }
        DbUtils.closeServer();
    }
}