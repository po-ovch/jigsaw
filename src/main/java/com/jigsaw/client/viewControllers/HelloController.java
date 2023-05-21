package com.jigsaw.client.viewControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.jigsaw.client.SharedComponents.server;

public class HelloController {
    public TextField playerNameField;

    @FXML
    public void onSubmitButtonClick() {
        if (!checkName(playerNameField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Player name");
            alert.setHeaderText("Player name must contain at least one symbol. " +
                            "And it mustn't contain \"\\n\" symbol.");
            alert.show();
            return;
        }
        if (!server.isOpen()) {
            // TODO: server disconnected view??
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Server connection");
            alert.setHeaderText("Can't connect to the server, check if it is running");
            alert.show();
        }

        server.registerPlayer(playerNameField.getText());
    }

    private static boolean checkName(String name) {
        return !name.trim().isEmpty() && !name.contains("\n");
    }
}