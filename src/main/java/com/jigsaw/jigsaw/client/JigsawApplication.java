package com.jigsaw.jigsaw.client;

import com.jigsaw.jigsaw.SocketWrapper;
import com.jigsaw.jigsaw.client.viewControllers.HelloController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class JigsawApplication extends Application {
    public static SocketWrapper clientSocket;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JigsawApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 376, 276);
        stage.setTitle("Jigsaw");
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> onClose());

        stage.show();

        HelloController.currentStage = stage;
    }

    private void onClose() {
        try {
            clientSocket.getWriter().write("exit");
            clientSocket.getWriter().flush();
        } catch (Exception ignored) {
        }
        clientSocket.close();
    }

}