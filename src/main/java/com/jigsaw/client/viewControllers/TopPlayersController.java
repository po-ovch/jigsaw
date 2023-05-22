package com.jigsaw.client.viewControllers;

import com.jigsaw.client.webControllers.HttpClient;
import com.jigsaw.shared.entities.GameStatistics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static com.jigsaw.client.SharedComponents.viewManager;

public class TopPlayersController implements Initializable {
    public TableView<GameStatistics> tableView;
    public Label problemTopLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        problemTopLabel.setVisible(false);
        createTable();

        var data = new HttpClient().getTopStatistics(10);
        if (data == null) {
            problemTopLabel.setVisible(true);
            tableView.setVisible(false);
            return;
        }
        ObservableList<GameStatistics> gameStatistics = FXCollections.observableArrayList(data);
        tableView.setItems(gameStatistics);
    }

    @FXML
    public void onReturnButtonClick() {
        viewManager.returnFromTopPlayers();
    }

    private void createTable() {
        var nameCol = new TableColumn<GameStatistics, String>("Player name");
        var endCol = new TableColumn<GameStatistics, String>("Finish DateTime");
        var movesMade = new TableColumn<GameStatistics, String>("Moves made");
        var timeSpent = new TableColumn<GameStatistics, String>("Time spent");

        tableView.getColumns().addAll(nameCol, endCol, movesMade, timeSpent);

        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("playerName")
        );
        endCol.setCellValueFactory(
                new PropertyValueFactory<>("endTime")
        );
        movesMade.setCellValueFactory(
                new PropertyValueFactory<>("figuresMappedNum")
        );
        timeSpent.setCellValueFactory(
                new PropertyValueFactory<>("playedTime")
        );
    }
}
