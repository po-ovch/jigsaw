package com.jigsaw.jigsaw.client;

import javafx.scene.layout.Pane;

public class Table extends Pane {
    public final PlayingCell[][] cells;

    public Table() {
        int rawSize = 9;
        int columnSize = 9;

        cells = new PlayingCell[rawSize][columnSize];
        double cellSize = (new PlayingCell()).getWidth();

        double startLocationY = 20;
        for (int i = 0; i < rawSize; i++) {
            double startLocationX = 20;
            for (int j = 0; j < columnSize; j++) {
                cells[i][j] = new PlayingCell();
                cells[i][j].setLayoutX(startLocationX);
                cells[i][j].setLayoutY(startLocationY);
                startLocationX += cellSize;
                getChildren().add(cells[i][j]);
            }
            startLocationY += cellSize;
        }
        setSides();
    }

    private void setSides() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (j > 0) {
                    cells[i][j].sides[3] = cells[i][j - 1];
                }
                if (j < 8) {
                    cells[i][j].sides[1] = cells[i][j + 1];
                }
                if (i > 0) {
                    cells[i][j].sides[2] = cells[i - 1][j];
                }
                if (i < 8) {
                    cells[i][j].sides[0] = cells[i + 1][j];
                }
            }
        }
    }
}
