package com.jigsaw.jigsaw.client.viewControllers;

import com.jigsaw.jigsaw.client.*;
import com.jigsaw.jigsaw.endpoint.shared.FigureGenerator;
import com.jigsaw.jigsaw.endpoint.shared.FigureInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static com.jigsaw.jigsaw.client.SharedComponents.settings;
import static com.jigsaw.jigsaw.client.viewControllers.HelloController.retryDueToPartner;
import static com.jigsaw.jigsaw.client.viewControllers.HelloController.spawnNewWindow;


public class GameController implements Initializable {
    public static Stage gameStage;
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
        GameController.timeline.stop();
        System.out.println(GameController.closingStatus);

        switch (GameController.closingStatus) {
            case ServerDisconnected -> spawnNewWindow("server-disconnected", "Server disconnected",
                    true);
            case PartnerDisconnected -> retryDueToPartner();
            case TimeEnded, Close -> {
                if (settings.isMultiplayer) {
                    spawnNewWindow("await-finish", "Wait for finish", true);
                }
                Platform.runLater(HelloController::sendStatistics);
            }
        }
        gameStage.close();
    }

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

    public void setTimer() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("The end");
        alert.setHeaderText("Game time is over");
        alert.show();
        closingStatus = ClosingStatus.TimeEnded;
        HelloController.currentStage.getOnCloseRequest().handle(null);
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
}
