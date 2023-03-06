package com.jigsaw.jigsaw.client;

import com.jigsaw.jigsaw.Result;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class HelloController {
    public static Stage currentStage;
    public static GameSettings settings;

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
    public TableView<Result> tableView;
    public Label problemTopLabel;

    private boolean wasStartPaneVisible;

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
        try {
            var writer = JigsawApplication.clientSocket.getWriter();

            writer.write(playerNameField.getText() + "\n");
            writer.flush();

            Platform.runLater(this::setup);
        } catch (IOException e) {
            registerPane.setVisible(false);
            serverDisconnectedPane.setVisible(true);
        }
    }

    private static boolean checkName(String name) {
        return !name.trim().isEmpty() && !name.contains("\n");
    }

    private void setup() {
        createTable();
        var reader = JigsawApplication.clientSocket.getReader();

        try {
            while (true) {
                var text = reader.readLine();
                if (text.equals("await")) {
                    registerPane.setVisible(false);
                    awaitPane.setVisible(true);
                } else if (text.equals("game")) {
                    setGameSettings(reader);
                    setPanesInvisible();
                    startGamePane.setVisible(true);
                    break;
                }
            }
        } catch (Exception e) {
            setPanesInvisible();
            serverDisconnectedPane.setVisible(true);
        }
    }

    private void createTable() {
        var loginCol = new TableColumn<Result, String>("Login");
        var endCol = new TableColumn<Result, String>("Finish DateTime");
        var movesMade = new TableColumn<Result, String>("Moves made");
        var timeSpent = new TableColumn<Result, String>("Time spent");

        tableView.getColumns().addAll(loginCol, endCol, movesMade, timeSpent);

        loginCol.setCellValueFactory(
                new PropertyValueFactory<>("playerName")
        );
        endCol.setCellValueFactory(
                new PropertyValueFactory<>("endTime")
        );
        movesMade.setCellValueFactory(
                new PropertyValueFactory<Result, String>("figuresMappedNum")
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
                Platform.runLater(this::makeResults);
            }
        }
    }

    private void makeResults() {
        setPanesInvisible();
        var reader = JigsawApplication.clientSocket.getReader();
        var writer = JigsawApplication.clientSocket.getWriter();

        var finish = LocalDateTime.now();
        long endTime = (new Date()).getTime();
        long difference = endTime - GameController.startTime;
        var timeFormatter = new SimpleDateFormat("ss");
        var time = timeFormatter.format(new Date(difference));
        var dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            writer.write("result\n");
            (new Result(settings.playerName, GameController.madeMovesCounter,
                    time, finish.format(dtFormatter))).writeResult(writer);
            writer.flush();
            var winner = reader.readLine();
            var playerName = reader.readLine();
            resultLabel.setText("Winner: " + winner + "\n" +
                    "Figures made: " + GameController.madeMovesCounter + "\n" +
                    "Overall game time: " + time);
            resultPlayerNameLabel.setText("Your name: " + playerName);
            resultPane.setVisible(true);
        } catch (Exception e) {
            setPanesInvisible();
            serverDisconnectedPane.setVisible(true);
        }
    }

    private void retryDueToPartner() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Win!");
        alert.setHeaderText("You are the winner because your partner left the game");
        alert.show();

        partnerDisconnectedPane.setVisible(true);
    }

    private void setGameSettings(BufferedReader reader) throws IOException {
        var playerName = reader.readLine();
        var isMultiplayer = reader.readLine();
        String rivalName = null;
        if (isMultiplayer.equals("true")) {
            rivalName = reader.readLine();
        }
        var gameDuration = Integer.parseInt(reader.readLine());
        settings = new GameSettings(playerName, isMultiplayer,
                rivalName, gameDuration);
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

        ObservableList<Result> results = FXCollections.observableArrayList();
        while (true) {
            try {
                var result = Result.readResult(reader);
                if (result != null) {
                    results.add(result);
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
        tableView.setItems(results);
    }

    private void setPanesInvisible() {
        registerPane.setVisible(false);
        awaitPane.setVisible(false);
        startGamePane.setVisible(false);
        resultPane.setVisible(false);
        topPlayersPane.setVisible(false);
    }

    public void onReturnButtonClick() {
        topPlayersPane.setVisible(false);

        if (wasStartPaneVisible) {
            startGamePane.setVisible(true);
        } else {
            resultPane.setVisible(true);
        }
    }
}