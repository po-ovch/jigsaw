package com.jigsaw.client.viewControllers;

import com.jigsaw.client.JigsawApplication;
import com.jigsaw.client.SharedComponents;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewManager {
    private final Stage stage;
    private Stage gameStage;
    private boolean hasPlayed;

    public ViewManager(Stage stage) {
        this.stage = stage;
        hasPlayed = false;
        welcome();
    }

    public void welcome() {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(JigsawApplication.class.getResource("hello-view.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 376, 276);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle("Jigsaw");
            stage.setScene(scene);
            stage.show();
        });
    }

    public void awaitPlayers() {
        changeScene("await-view.fxml");
    }

    public void readyToStart() {
        changeScene("start-game-view.fxml");
    }

    public void playGame() {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(JigsawApplication.class.getResource("game-view.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
                gameStage = new Stage();
                gameStage.setMinHeight(380);
                gameStage.setMinWidth(500);
                gameStage.setResizable(false);
                gameStage.initModality(Modality.APPLICATION_MODAL);
                gameStage.setTitle("Game");
                gameStage.setScene(scene);
                gameStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void closeGame() {
        gameStage.close();
        hasPlayed = true;

        switch (GameController.closingStatus) {
            case ServerDisconnected -> serverDisconnected();
            case PartnerDisconnected -> retryDueToPartner();
            case TimeEnded, Close -> {
                if (SharedComponents.settings.isMultiplayer) {
                    awaitFinish();
                }
            }
        }
    }

    public void serverDisconnected() {
        changeScene("server-disconnected-view.fxml");
    }

    public void retryDueToPartner() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Win!");
            alert.setHeaderText("You are the winner because your partner left the game");
            alert.show();
        });
        changeScene("partner-disconnected-view.fxml");
    }

    public void awaitFinish() {
        changeScene("await-finish-view.fxml");
    }

    public void showResults() {
        changeScene("result-view.fxml");
    }

    public void showTopPlayers() {
        changeScene("top-players-view.fxml");
    }

    public void returnFromTopPlayers() {
        if (hasPlayed) {
            showResults();
        } else {
            readyToStart();
        }
    }

    public void closeApp() {
        stage.close();
    }

    private void changeScene(String fxmlSceneName) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(JigsawApplication.class.getResource(fxmlSceneName));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
