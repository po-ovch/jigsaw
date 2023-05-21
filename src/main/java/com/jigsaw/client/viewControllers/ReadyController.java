package com.jigsaw.client.viewControllers;

import javafx.fxml.FXML;

import static com.jigsaw.client.SharedComponents.viewManager;

public class ReadyController {

    @FXML
    protected void onStartButtonClick() {
        viewManager.playGame();
    }

    public void onTopButtonClick() {
        viewManager.showTopPlayers();
    }
}
