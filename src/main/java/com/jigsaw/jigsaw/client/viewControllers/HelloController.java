package com.jigsaw.jigsaw.client.viewControllers;

import com.jigsaw.jigsaw.endpoint.shared.GameStatistics;
import com.jigsaw.jigsaw.client.JigsawApplication;
import com.jigsaw.jigsaw.endpoint.shared.Result;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.jigsaw.jigsaw.client.SharedComponents.server;
import static com.jigsaw.jigsaw.client.SharedComponents.settings;

public class HelloController {
    public static Stage currentStage;

    public VBox registerPane;
    public VBox awaitPane;
    public VBox startGamePane;
    public VBox resultPane;
    public VBox topPlayersPane;

    public Button startGameButton;
    public Button quitButton;
    public TextField playerNameField;
    public VBox serverDisconnectedPane;
    public static Label resultLabel;
    public VBox partnerDisconnectedPane;
    public VBox awaitFinishPane;
    public static Label resultPlayerNameLabel;
    public TableView<GameStatistics> tableView;
    public Label problemTopLabel;

    private boolean wasStartPaneVisible;

    private static Stage spawnNewWindow(String path, String title) {
        FXMLLoader fxmlLoader
                = new FXMLLoader(JigsawApplication.class.getResource(path + "-view" + ".fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = new Stage();
        stage.setMinHeight(380);
        stage.setMinWidth(500);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(scene);

//        stage.setOnCloseRequest(windowEvent -> onClosing());
        stage.show();
        currentStage.close();
        return stage;
    }

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
        server.registerPlayer(playerNameField.getText());
        createTable();
    }

    private static boolean checkName(String name) {
        return !name.trim().isEmpty() && !name.contains("\n");
    }

    public static void awaitGame() {
        currentStage = spawnNewWindow("await", "Jigsaw");
    }

    public static void startGame() {
        currentStage = spawnNewWindow("start-game", "Game");
    }

    @FXML
    protected void onStartButtonClick() {
        currentStage = spawnNewWindow("game", "Game");

        startGameButton.setText("Play again");
        quitButton.setVisible(true);
    }

    public void onQuitButtonClick() {
        JigsawApplication.startStage.close();
    }

    private void onClosing() {
        GameController.timeline.stop();
        currentStage.close();

        switch (GameController.closingStatus) {
            case ServerDisconnected ->
                    currentStage = spawnNewWindow("server-disconnected", "Server disconnected");
            case PartnerDisconnected -> retryDueToPartner();
            case TimeEnded, Close -> {
                if (settings.isMultiplayer) {
                    currentStage = spawnNewWindow("await-finish", "Game");
                }
                Platform.runLater(this::sendStatistics);
            }
        }
    }

    private void sendStatistics() {
        var finish = LocalDateTime.now();
        long endTime = (new Date()).getTime();
        long difference = endTime - GameController.startTime;
        var timeFormatter = new SimpleDateFormat("ss");
        var time = timeFormatter.format(new Date(difference));
        var dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        var result = new GameStatistics(settings.playerName, GameController.madeMovesCounter,
                time, finish.format(dtFormatter));
        server.sendResult(result);
    }

    public static void showResults(Result result) {
        Platform.runLater(() -> {
            resultLabel.setText("Winner: " + result.winnerName + "\n" +
                    "Figures made: " + GameController.madeMovesCounter + "\n" +
                    "Overall game time: " + result.overallTime);
            resultPlayerNameLabel.setText("Your name: " + result.playerName);
            currentStage = spawnNewWindow("result", "Results");
        });
    }

    private void retryDueToPartner() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Win!");
        alert.setHeaderText("You are the winner because your partner left the game");
        alert.show();

        currentStage = spawnNewWindow("partner-disconnected", "Partner disconnected");
    }

    public void onWaitForPartnerButtonClick() {
        currentStage = spawnNewWindow("await", "Wait");
    }

    public void onTopButtonClick() {
        wasStartPaneVisible = startGamePane.visibleProperty().get();
        currentStage = spawnNewWindow("top-players", "Wait");

        var writer = JigsawApplication.clientSocket.getWriter();
        var reader = JigsawApplication.clientSocket.getReader();

        try {
            writer.write("topPlayers\n");
            writer.flush();
        } catch (IOException e) {
            currentStage = spawnNewWindow("server-disconnected", "Wait");
            return;
        }

        ObservableList<GameStatistics> gameStatistics = FXCollections.observableArrayList();
        while (true) {
            try {
                var result = GameStatistics.readResult(reader);
                if (result != null) {
                    gameStatistics.add(result);
                } else {
                    break;
                }
            } catch (IOException e) {
                currentStage = spawnNewWindow("server-disconnected", "Wait");
                return;
            } catch (RuntimeException e) {
                problemTopLabel.setVisible(true);
                tableView.setVisible(false);
                return;
            }
        }
        tableView.setItems(gameStatistics);
    }

    public void onReturnButtonClick() {
        if (wasStartPaneVisible) {
            currentStage = spawnNewWindow("start-game", "Jigsaw");
        } else {
            currentStage = spawnNewWindow("result-game", "Results");
        }
    }

    private void createTable() {
        var loginCol = new TableColumn<GameStatistics, String>("Login");
        var endCol = new TableColumn<GameStatistics, String>("Finish DateTime");
        var movesMade = new TableColumn<GameStatistics, String>("Moves made");
        var timeSpent = new TableColumn<GameStatistics, String>("Time spent");

        tableView.getColumns().addAll(loginCol, endCol, movesMade, timeSpent);

        loginCol.setCellValueFactory(
                new PropertyValueFactory<>("playerName")
        );
        endCol.setCellValueFactory(
                new PropertyValueFactory<>("endTime")
        );
        movesMade.setCellValueFactory(
                new PropertyValueFactory<GameStatistics, String>("figuresMappedNum")
        );
        timeSpent.setCellValueFactory(
                new PropertyValueFactory<>("playedTime")
        );
    }
}