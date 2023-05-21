package com.jigsaw.client.viewControllers;

import com.jigsaw.client.ClosingStatus;
import com.jigsaw.client.SharedComponents;
import com.jigsaw.shared.entities.FigureGenerator;
import com.jigsaw.shared.entities.FigureInfo;
import com.jigsaw.shared.entities.GameStatistics;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static com.jigsaw.client.SharedComponents.*;


public class GameController implements Initializable {
    public static ClosingStatus closingStatus;
    public Pane figurePane;

    // We need the access to these fields in event handlers,
    // so fields are duplicated and made static.
    public static Pane figuresParentPane;

    public AnchorPane pane;
    public Label timeLabel;
    public Button endGameButton;

    public static DateFormat timeFormatter;
    public static Timeline timeline;
    public static long startTime;
    public static int madeMovesCounter;
    public VBox infoBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        madeMovesCounter = 0;

        timeFormatter = new SimpleDateFormat("HH:mm:ss");
        timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        startTime = (new Date()).getTime();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                event -> timeLabel.setText("Current time: " +
                        timeFormatter.format(new Date((new Date()).getTime() - startTime)))));
        timeline.setCycleCount(settings.gameDuration);
        timeline.setOnFinished(actionEvent -> setTimer());
        timeline.play();

        setInfo();
        figuresParentPane = figurePane;
        setFigure(SharedComponents.firstFigure);
    }

    public static void setFigure(FigureInfo figureInfo) {
        var figure = FigureGenerator.create(figureInfo);
        Platform.runLater(() -> {
            var boxChildren = figuresParentPane.getChildren();
            boxChildren.addAll(figure.getCells());
        });
    }

    @FXML
    public void onEndButtonClick() {
        closingStatus = ClosingStatus.Close;
        onClose();
    }

    public void setTimer() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("The end");
        alert.setHeaderText("Game time is over");
        alert.show();
        closingStatus = ClosingStatus.TimeEnded;
        onClose();
    }

    private void setInfo() {
        infoBox.setMinHeight(40);

        var playerNameLabel = new Label("Player name: " + settings.playerName);
        infoBox.getChildren().add(playerNameLabel);

        if (settings.rivalName != null) {
            var rivalNameLabel = new Label("Rival name: " + settings.rivalName);
            infoBox.getChildren().add(rivalNameLabel);
        }

        var maxTimeLabel = new Label("Max time: " + settings.gameDuration + " sec");
        infoBox.getChildren().add(maxTimeLabel);
    }

    private static void onClose() {
        timeline.stop();
        viewManager.closeGame();

        if (closingStatus == ClosingStatus.Close || closingStatus == ClosingStatus.TimeEnded) {
            var statistics = gatherStatistics();
            server.sendResult(statistics);
        }
    }

    private static GameStatistics gatherStatistics() {
        var finish = LocalDateTime.now();
        long endTime = (new Date()).getTime();
        long difference = endTime - GameController.startTime;
        var timeFormatter = new SimpleDateFormat("ss");
        var time = timeFormatter.format(new Date(difference));
        var dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new GameStatistics(settings.playerName, GameController.madeMovesCounter,
                time, finish.format(dtFormatter));
    }
}
