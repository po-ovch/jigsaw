package com.jigsaw.client.viewControllers;

import com.jigsaw.client.JigsawApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static com.jigsaw.client.SharedComponents.result;
import static com.jigsaw.client.SharedComponents.viewManager;

public class ResultController implements Initializable {
    public Label resultPlayerNameLabel;
    public Label resultLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resultLabel.setText("Winner: " + result.winnerName + "\n" +
                "Figures made: " + GameController.madeMovesCounter + "\n" +
                "Overall game time: " + result.overallTime);
        resultPlayerNameLabel.setText("Your name: " + result.playerName);
    }

    @FXML
    protected void onStartButtonClick() {
        viewManager.playGame();
    }

    @FXML
    public void onTopButtonClick() {
        viewManager.showTopPlayers();
    }

    @FXML
    public void onQuitButtonClick() {
        viewManager.closeApp();
    }
}
