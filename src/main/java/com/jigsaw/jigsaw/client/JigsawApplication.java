package com.jigsaw.jigsaw.client;

import com.jigsaw.jigsaw.SocketWrapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class JigsawApplication extends Application {
    public static Stage startStage;
    public static SocketWrapper clientSocket;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JigsawApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 376, 276);
        stage.setTitle("Jigsaw");
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> onClose());

        stage.show();

        startStage = stage;
    }

    private void onClose() {
        try {
            clientSocket.getWriter().write("exit");
            clientSocket.getWriter().flush();
        } catch (Exception ignored) {
        }
        clientSocket.close();
    }

    public static void main(String[] args) {
        launch();
    }
}