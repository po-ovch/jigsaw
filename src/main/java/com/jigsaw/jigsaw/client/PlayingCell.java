package com.jigsaw.jigsaw.client;

import com.jigsaw.jigsaw.client.viewControllers.GameController;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;

public class PlayingCell extends Rectangle {
    public PlayingCell[] sides;
    private boolean isOccupied;

    public PlayingCell() {
        super(30, 30);
        setFill(Color.valueOf("#FFE9C8"));
        setStroke(Color.BLACK);
        isOccupied = false;
        sides = new PlayingCell[4];

        setDraggingEvents();
    }

    private void setDraggingEvents() {
        setOnDragOver(dragEvent -> {
            if (dragEvent.getDragboard().hasString()) {
                dragEvent.acceptTransferModes(TransferMode.ANY);
            }
        });
        setOnDragDropped(dragEvent -> {
            FigureCell source = (FigureCell) dragEvent.getGestureSource();
            var list = source.checkLocation(this);
            if (list != null) {
                for (var cell1 : list) {
                    cell1.setFill(Color.RED);
                }
                GameController.madeMovesCounter++;
                GameController.figuresParentPane.getChildren().clear();
                SharedComponents.server.requestFigure();
            } else {
                source.unputFigure();
            }
        });
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean set) {
        isOccupied = set;
    }
}
