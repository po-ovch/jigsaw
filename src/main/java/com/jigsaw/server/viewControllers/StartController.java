package com.jigsaw.server.viewControllers;

import com.jigsaw.server.JigsawServerApplication;
import com.jigsaw.server.webControllers.JigsawServerEndpoint;
import com.jigsaw.server.SharedComponents;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.glassfish.tyrus.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class StartController {

    public static Stage currentStage;

    public VBox startPane;
    public VBox runningPane;
    public VBox errorPane;
    public Spinner<Integer> playersNumSpinner;
    public Slider gameDurationSlider;

    public static ServerSocket server;
    public static Thread gameSetupThread;

    @FXML
    public void onRunButtonClick() throws IOException {
        SharedComponents.manager.setPlayersNum(playersNumSpinner.getValue());
        SharedComponents.manager.setGameDuration((int) gameDurationSlider.getValue());

        final boolean result = runServer();
        FXMLLoader fxmlLoader;
        Stage stage = new Stage();

        if (result) {
            fxmlLoader =
                    new FXMLLoader(JigsawServerApplication.class.getResource("running-view" + ".fxml"));
            stage.setTitle("Server is running");

        } else {
            fxmlLoader =
                    new FXMLLoader(JigsawServerApplication.class.getResource("error-view" +
                            ".fxml"));
            stage.setTitle("Server error");
        }
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> onClosing());
        stage.setScene(scene);
        stage.show();

        currentStage = stage;
        JigsawServerApplication.startStage.close();
    }

    private boolean runServer() {
        var set = new HashSet<Class<?>>();
        set.add(JigsawServerEndpoint.class);

        try {
            var server = new Server("localhost", 7001, "/websockets", null, set);
            server.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void onClosing() {
        currentStage.close();
    }

}
