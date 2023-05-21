package com.jigsaw.client;

import java.util.List;
import java.util.Objects;

public class Figure {
    /**
     * Figure root. Many operations start from this cell.
     */
    private final FigureCell root;

    /**
     * All cells that are included in figure.
     */
    private final List<FigureCell> cells;

    public Figure(FigureCell newRoot) {
        root = newRoot;
        cells = newRoot.getAllCells();
    }

    /**
     * Creates new instance of figure
     * that is rotated counterclockwise
     * in comparison with this instance.
     * @return New rotated figure.
     */
    public Figure rotate() {
        FigureCell newRoot = new FigureCell();
        root.rotate(newRoot);
        return new Figure(newRoot);
    }

    /**
     * G-type:
     *   | | |
     *   | |
     *   | |
     */
    public static Figure createGType() {
        var root = new FigureCell();

        var second = root.createSideCell("left");
        var third = second.createSideCell("bottom");
        third.createSideCell("bottom");
        return new Figure(root);
    }

    /**
     * Symmetric G-type:
     *  | | |
     *    | |
     *    | |
     */
    public static Figure createSymmetricGType() {
        var root = new FigureCell();

        var second = root.createSideCell("right");
        var third = second.createSideCell("bottom");
        third.createSideCell("bottom");
        return new Figure(root);
    }

    /**
     * Z-type:
     *    | | |
     *  | | |
     */
    public static Figure createZType() {
        var root = new FigureCell();

        var second = root.createSideCell("left");
        var third = second.createSideCell("bottom");
        third.createSideCell("left");
        return new Figure(root);
    }

    /**
     * Symmetric Z-type:
     *    | | |
     *  | | |
     */
    public static Figure createSymmetricZType() {
        var root = new FigureCell();

        var second = root.createSideCell("right");
        var third = second.createSideCell("bottom");
        third.createSideCell("right");
        return new Figure(root);
    }

    /**
     * Big Corner type:
     *  | |
     *  | |
     *  | | | |
     */
    public static Figure createBigCornerType() {
        var root = new FigureCell();

        var second = root.createSideCell("bottom");
        var third = second.createSideCell("bottom");
        var forth = third.createSideCell("right");
        forth.createSideCell("right");
        return new Figure(root);
    }

    /**
     * T-type:
     *  | | | |
     *    | |
     *    | |
     */
    public static Figure createTType() {
        var root = new FigureCell();

        var second = root.createSideCell("right");
        second.createSideCell("right");
        var forth = second.createSideCell("bottom");
        forth.createSideCell("bottom");
        return new Figure(root);
    }

    /**
     * Line type:
     *  | |
     *  | |
     *  | |
     */
    public static Figure createLineType() {
        var root = new FigureCell();

        var second = root.createSideCell("bottom");
        second.createSideCell("bottom");
        return new Figure(root);
    }

    /**
     * Point type:
     *  | |
     */
    public static Figure createPointType() {
        var root = new FigureCell();

        return new Figure(root);
    }

    /**
     * Small Corner type:
     *  | |
     *  | | |
     */
    public static Figure createSmallCornerType() {
        var root = new FigureCell();

        var second = root.createSideCell("bottom");
        second.createSideCell("right");
        return new Figure(root);
    }

    /**
     * Unfinished crossroads type:
     *  | |
     *  | | |
     *  | |
     */
    public static Figure createUnfinishedCrossroadsType() {
        var root = new FigureCell();

        var second = root.createSideCell("bottom");
        second.createSideCell("right");
        second.createSideCell("bottom");
        return new Figure(root);
    }

    public List<FigureCell> getCells() {
        return cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Figure figure = (Figure) o;

        // We do not need to compare all cells because
        // they are recursively connected to the root cell.
        return Objects.equals(root, figure.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(root, getCells());
    }

    public FigureCell getRoot() {
        return root;
    }
}
