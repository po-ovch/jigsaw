package com.jigsaw.server.viewControllers;

import com.jigsaw.server.JigsawServerApplication;
import com.jigsaw.server.webControllers.WebsocketServerHandler;
import com.jigsaw.server.SharedComponents;
import com.jigsaw.server.webControllers.HttpHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.glassfish.tyrus.server.Server;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.Executors;

public class StartController {

    public static Stage currentStage;

    public VBox startPane;
    public VBox runningPane;
    public VBox errorPane;
    public Spinner<Integer> playersNumSpinner;
    public Slider gameDurationSlider;

    public static ServerSocket server;

    @FXML
    public void onRunButtonClick() throws IOException {
        SharedComponents.manager.setPlayersNum(playersNumSpinner.getValue());
        SharedComponents.manager.setGameDuration((int) gameDurationSlider.getValue());

        final boolean result = runWebsocketServer();
        runHttpServer();
        if (result) {
            nextScene("running-view", "Server is running");
        } else {
            nextScene("error-view", "Server error");
        }

        JigsawServerApplication.startStage.close();
    }

    private void nextScene(String sceneName, String title) throws IOException {
        FXMLLoader fxmlLoader;
        Stage stage = new Stage();
        fxmlLoader =
                new FXMLLoader(JigsawServerApplication.class.getResource(sceneName + ".fxml"));
        stage.setTitle(title);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> onClosing());
        stage.setScene(scene);
        stage.show();

        currentStage = stage;
    }

    private boolean runWebsocketServer() {
        var set = new HashSet<Class<?>>();
        set.add(WebsocketServerHandler.class);

        try {
            var server = new Server("localhost", 7001, "/websockets", null, set);
            server.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void runHttpServer() throws IOException {
        var server = HttpServer.create(new InetSocketAddress("localhost", 7001), 0);
        server.createContext("/players", new HttpHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
    }

    private void onClosing() {
        currentStage.close();
    }

}
