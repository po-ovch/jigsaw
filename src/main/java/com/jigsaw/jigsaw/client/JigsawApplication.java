package com.jigsaw.jigsaw.client;

import com.jigsaw.jigsaw.SocketWrapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class JigsawApplication extends Application {
    public static Stage startStage;
    public static SocketWrapper clientSocket;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            clientSocket = new SocketWrapper(new Socket("127.0.0.1", 5000));
        } catch (IOException exception) {
            System.out.println("Error while connecting server. Try again later");
            System.exit(0);
        }

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