package com.jigsaw.client.viewControllers;

import com.jigsaw.shared.entities.GameStatistics;
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
        createTable();

        // TODO: fill table

        /*ObservableList<GameStatistics> gameStatistics = FXCollections.observableArrayList();
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
        tableView.setItems(gameStatistics);*/
    }

    @FXML
    public void onReturnButtonClick() {
        viewManager.returnFromTopPlayers();
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
                new PropertyValueFactory<>("figuresMappedNum")
        );
        timeSpent.setCellValueFactory(
                new PropertyValueFactory<>("playedTime")
        );
    }
}
