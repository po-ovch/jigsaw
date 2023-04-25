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

    // TODO: del when refactor views
    public static VBox registerPane1;
    public static VBox awaitPane1;
    public static VBox startGamePane1;
    public static VBox resultPane1;
    public static VBox topPlayersPane1;

    public static Label resultLabel1;
    public static Label resultPlayerNameLabel1;

    public VBox registerPane;
    public VBox awaitPane;
    public VBox startGamePane;
    public VBox resultPane;
    public VBox topPlayersPane;

    public Button startGameButton;
    public Button quitButton;
    public TextField playerNameField;
    public VBox serverDisconnectedPane;
    public Label resultLabel;
    public VBox partnerDisconnectedPane;
    public VBox awaitFinishPane;
    public Label resultPlayerNameLabel;
    public TableView<GameStatistics> tableView;
    public Label problemTopLabel;

    private boolean wasStartPaneVisible;

    @FXML
    public void onSubmitButtonClick() {
        start();

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
        registerPane1.setVisible(false);
        awaitPane1.setVisible(true);
    }

    public static void startGame() {
        setPanesInvisible();
        startGamePane1.setVisible(true);
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

    @FXML
    protected void onStartButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JigsawApplication.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setMinHeight(380);
        stage.setMinWidth(500);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Game");
        stage.setScene(scene);

        stage.setOnCloseRequest(windowEvent -> onClosing());
        stage.show();

        resultPane.setVisible(false);
        startGameButton.setText("Play again");
        quitButton.setVisible(true);

        currentStage = stage;
    }

    public void onQuitButtonClick() {
        JigsawApplication.startStage.close();
    }

    private void onClosing() {
        GameController.timeline.stop();
        currentStage.close();
        setPanesInvisible();

        switch (GameController.closingStatus) {
            case ServerDisconnected -> serverDisconnectedPane.setVisible(true);
            case PartnerDisconnected -> retryDueToPartner();
            case TimeEnded, Close -> {
                if (settings.isMultiplayer) {
                    awaitFinishPane.setVisible(true);
                }
                Platform.runLater(this::sendStatistics);
            }
        }
    }

    private void sendStatistics() {
        setPanesInvisible();

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
            resultLabel1.setText("Winner: " + result.winnerName + "\n" +
                    "Figures made: " + GameController.madeMovesCounter + "\n" +
                    "Overall game time: " + result.overallTime);
            resultPlayerNameLabel1.setText("Your name: " + result.playerName);
            resultPane1.setVisible(true);
        });
    }

    private void retryDueToPartner() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Win!");
        alert.setHeaderText("You are the winner because your partner left the game");
        alert.show();

        partnerDisconnectedPane.setVisible(true);
    }

    public void onWaitForPartnerButtonClick() {
        partnerDisconnectedPane.setVisible(false);
        awaitPane.setVisible(true);
    }

    public void onTopButtonClick() {
        wasStartPaneVisible = startGamePane.visibleProperty().get();
        setPanesInvisible();
        problemTopLabel.setVisible(false);
        topPlayersPane.setVisible(true);
        var writer = JigsawApplication.clientSocket.getWriter();
        var reader = JigsawApplication.clientSocket.getReader();

        try {
            writer.write("topPlayers\n");
            writer.flush();
        } catch (IOException e) {
            serverDisconnectedPane.setVisible(true);
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
                serverDisconnectedPane.setVisible(true);
                return;
            } catch (RuntimeException e) {
                problemTopLabel.setVisible(true);
                tableView.setVisible(false);
                return;
            }
        }
        tableView.setItems(gameStatistics);
    }

    private static void setPanesInvisible() {
        registerPane1.setVisible(false);
        awaitPane1.setVisible(false);
        startGamePane1.setVisible(false);
        resultPane1.setVisible(false);
        topPlayersPane1.setVisible(false);
    }

    public void onReturnButtonClick() {
        topPlayersPane.setVisible(false);

        if (wasStartPaneVisible) {
            startGamePane.setVisible(true);
        } else {
            resultPane.setVisible(true);
        }
    }

    // TODO: del when refactoring
    public void start() {
        registerPane1 = registerPane;
        awaitPane1 = awaitPane;
        startGamePane1 = startGamePane;
        resultPane1 = resultPane;
        topPlayersPane1 = topPlayersPane;

        resultLabel1 = resultLabel;
        resultPlayerNameLabel1 = resultPlayerNameLabel;
    }
}