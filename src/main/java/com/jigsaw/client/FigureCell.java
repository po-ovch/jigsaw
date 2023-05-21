package com.jigsaw.client;

import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;


/**
 * Class that describes cell in figure.
 */
public class FigureCell extends Rectangle {
    /**
     * This field is needed when we check
     * whether the figure can be placed on playing field.
     * It helps to avoid getting in cycle (for instance, this
     * might happen in T-type figure).
     */
    private boolean isPut;

    /**
     * Similarly to the isPut field, this one is needed
     * to avoid cycles, but used when we collect all cells
     * in figure.
     */
    private boolean isCollected;

    /**
     * Similarly to the isPut field, this one is needed
     * to avoid cycles, but used when we compare two cells.
     */
    private boolean isCompared;


    /**
     * The array that describes the side cells of this cell.
     * The order:
     * bottom right top left
     * 0      1     2   3
     */
    private final FigureCell[] sides;

    /**
     * The array is needed to avoid cycles
     * when we rotate side cells around this cell.
     * The order of sided is similar to the sides field.
     */
    private boolean[] isRotated;

    public FigureCell() {
        super(30, 30);

        setFill(Color.AQUA);
        setStroke(Color.GRAY);
        setLayoutX(60);
        setLayoutY(80);

        sides = new FigureCell[4];
        setupRotated();
        isPut = false;
        isCollected = false;
        isCompared = false;

        setOnDragDetected(mouseEvent -> {
            Dragboard db = startDragAndDrop(TransferMode.MOVE);

            ClipboardContent cb = new ClipboardContent();
            cb.putString("0 1");

            db.setContent(cb);
            mouseEvent.consume();
        });
    }

    private void setupRotated() {
        isRotated = new boolean[]{false, false, false, false};
    }

    public FigureCell createSideCell(String sideName) {
        return createSideCell(getSideIndexFromName(sideName));
    }

    private int getSideIndexFromName(String sideName) {
        return switch (sideName) {
            case "bottom" -> 0;
            case "right" -> 1;
            case "top" -> 2;
            case "left" -> 3;
            default -> throw new RuntimeException("No side with name " + sideName);
        };
    }

    private FigureCell createSideCell(int sideIndex) {
        sides[sideIndex] = new FigureCell();
        sides[sideIndex].sides[(sideIndex + 2) % 4] = this;

        // default size of cell.
        int size = 30;
        sides[sideIndex].setLayoutX(getLayoutX() + ((sideIndex - 2) % 2) * (-1) * size);
        sides[sideIndex].setLayoutY(getLayoutY() + ((sideIndex - 1) % 2) * (-1) * size);
        return sides[sideIndex];
    }

    public void rotate(FigureCell newGenerated) {
        for (int sideIndex = 0; sideIndex < 4; sideIndex++) {
            if (!isRotated[sideIndex] && sides[sideIndex] != null) {
                newGenerated.createSideCell((sideIndex + 1) % 4);
                isRotated[sideIndex] = true;
                sides[sideIndex].isRotated[(sideIndex + 2) % 4] = true;
                sides[sideIndex].rotate(newGenerated.sides[(sideIndex + 1) % 4]);
            }
        }
        setupRotated();
    }

    /**
     * Checks whether all cells in figure can be placed
     * on playing field.
     * @param fieldCell Matching begins with this fieldCell
     *                  and this instance of Field cell.
     * @return List of playing cells on which figure would be placed.
     *  Or null if figure can not be placed.
     */
    public List<PlayingCell> checkLocation(PlayingCell fieldCell) {
        if (isPut) {
            // We have already placed this figure cell, so avoid cycle.
            return new ArrayList<>();
        } else if (fieldCell == null || fieldCell.isOccupied()) {
            // We got out of playing field or this fieldCell has been already set,
            // so we can not place figure there.
            return null;
        }
        List<PlayingCell> playingCells = new ArrayList<>();
        fieldCell.setOccupied(true);
        isPut = true;
        playingCells.add(fieldCell);
        for (int i = 0; i < 4; i++) {
            if (sides[i] != null) {
                // Recursively checking sides cell.
                var returnList = sides[i].checkLocation(fieldCell.sides[i]);
                if (returnList == null) {
                    // If figure can not be placed, we need to unset playingCells
                    // before returning null.
                    for (var playingCell : playingCells) {
                        playingCell.setOccupied(false);
                    }
                    return null;
                } else {
                    playingCells.addAll(returnList);
                }
            }
        }
        return playingCells;
    }

    /**
     * Collecting recursively all cells in the figure.
     * @return Cells in figure.
     */
    public List<FigureCell> getAllCells() {
        if (isCollected) {
            return new ArrayList<>();
        }
        List<FigureCell> figureCells = new ArrayList<>();
        figureCells.add(this);
        isCollected = true;
        for (int i = 0; i < 4; i++) {
            if (sides[i] != null) {
                var returnList = sides[i].getAllCells();
                figureCells.addAll(returnList);
            }
        }
        isCollected = false;
        return figureCells;
    }

    /**
     * If location trial was unsuccessful, we need to unset
     * isPut field, so the previous attempt of location
     * would not affect the following.
     */
    public void unputFigure() {
        var figureCells = getAllCells();
        for (var cell : figureCells) {
            cell.isPut = false;
        }
    }

    public FigureCell[] getSides() {
        return sides;
    }
}
