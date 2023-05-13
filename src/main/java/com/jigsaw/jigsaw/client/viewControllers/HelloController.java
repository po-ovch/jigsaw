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
import java.util.Objects;

import static com.jigsaw.jigsaw.client.SharedComponents.server;
import static com.jigsaw.jigsaw.client.SharedComponents.settings;

public class HelloController {
    public static Stage currentStage;

    public VBox startGamePane;
    public Button startGameButton;
    public TextField playerNameField;
    public static Label resultLabel;
    public static Label resultPlayerNameLabel;
    public TableView<GameStatistics> tableView;

    private boolean wasStartPaneVisible;

    protected static void spawnNewWindow(String path, String title, boolean closePrevStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(JigsawApplication.class.getResource(path + "-view" + ".fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Platform.runLater(
                    () -> {
                        Stage stage = new Stage();

                        stage.setResizable(false);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setTitle(title);
                        stage.setScene(scene);
                        stage.show();
                        if (currentStage.isShowing() && closePrevStage) {
                            System.out.println("PRINT");
                            System.out.println(currentStage);
                            System.out.println(currentStage.isShowing());
                            currentStage.close();

                            System.out.println("PRINT2");
                        }
                        if (Objects.equals(path, "game")) {
                            GameController.gameStage = stage;
                        } else {
                            currentStage = stage;
                        }
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Первое окно
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
    }

    private static boolean checkName(String name) {
        return !name.trim().isEmpty() && !name.contains("\n");
    }

    public static void awaitGame() {
        spawnNewWindow("await", "Wait for game", true);
    }

    public static void startGame() {
        spawnNewWindow("start-game", "Start the game", true);
    }

    // Второе окно
    @FXML
    protected void onStartButtonClick() {
        spawnNewWindow("game", "Game", true);

        startGameButton.setText("Play again");
    }

    public void onQuitButtonClick() {
        currentStage.close();
    }

    protected static void sendStatistics() {
        var finish = LocalDateTime.now();
        long endTime = (new Date()).getTime();
        long difference = endTime - GameController.startTime;
        var timeFormatter = new SimpleDateFormat("ss");
        var time = timeFormatter.format(new Date(difference));
        var dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        var result = new GameStatistics(settings.playerName, GameController.madeMovesCounter,
                time, finish.format(dtFormatter));
        server.sendResult(result);
        System.out.println("HUI1");
    }

    public static void showResults(Result result) {
        System.out.println("HUI");
        Platform.runLater(() -> {
            resultLabel.setText("Winner: " + result.winnerName + "\n" +
                    "Figures made: " + GameController.madeMovesCounter + "\n" +
                    "Overall game time: " + result.overallTime);
            resultPlayerNameLabel.setText("Your name: " + result.playerName);
            spawnNewWindow("result", "Results", true);
        });
    }

    protected static void retryDueToPartner() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Win!");
        alert.setHeaderText("You are the winner because your partner left the game");
        alert.show();

        spawnNewWindow("partner-disconnected", "Partner disconnected", true);
    }

    public void onWaitForPartnerButtonClick() {
        spawnNewWindow("await", "Wait for game", true);
    }

    public void onTopButtonClick() {
        wasStartPaneVisible =
                currentStage.getScene().lookup("startGamePane") != null;
        System.out.println(wasStartPaneVisible);

        var writer = JigsawApplication.clientSocket.getWriter();
        var reader = JigsawApplication.clientSocket.getReader();

        try {
            writer.write("topPlayers\n");
            writer.flush();
        } catch (IOException e) {
            spawnNewWindow("server-disconnected", "Server disconnected", true);
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
                spawnNewWindow("server-disconnected", "Server disconnected", true);
                return;
            } catch (RuntimeException e) {
                return;
            }
        }
        tableView.setItems(gameStatistics);
        createTable();
        spawnNewWindow("top-players", "Top players", true);
    }

    public void onReturnButtonClick() {
        if (wasStartPaneVisible) {
            spawnNewWindow("start-game", "Start the game", true);
        } else {
            spawnNewWindow("result", "Results", true);
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