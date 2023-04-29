package com.jigsaw.jigsaw.server.viewControllers;

import com.jigsaw.jigsaw.server.webControllers.JigsawServerEndpoint;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import org.glassfish.tyrus.server.Server;

import java.net.ServerSocket;
import java.util.*;

import static com.jigsaw.jigsaw.server.SharedComponents.manager;

public class StartController {
    public VBox startPane;
    public Spinner<Integer> playersNumSpinner;
    public Slider gameDurationSlider;

    public static ServerSocket server;
    public static Thread gameSetupThread;

    public VBox mistakePane;
    public VBox runningPane;

    public void onRunButtonClick() {
        manager.setPlayersNum(playersNumSpinner.getValue());
        manager.setGameDuration((int) gameDurationSlider.getValue());
        startPane.setVisible(false);
        runningPane.setVisible(true);

        runServer();
    }

    private void runServer() {
        var set = new HashSet<Class<?>>() ;
        set.add(JigsawServerEndpoint.class);

        try {
            var server = new Server("localhost", 7001, "/websockets", null, set);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
