package com.jigsaw.jigsaw.shared.entities;

import com.jigsaw.jigsaw.client.Figure;

import java.util.Random;


public class FigureGenerator {
    private static final Random random = new Random();

    public static FigureInfo generate() {
        int figureIndex = random.nextInt(0, 10);
        int rotateIndex = random.nextInt(0, 4);
        return new FigureInfo(figureIndex, rotateIndex);
    }

    private static Figure createFigureByIndex(int figureIndex) {
        return switch (figureIndex) {
            case 0 -> Figure.createGType();
            case 1 -> Figure.createSymmetricGType();
            case 2 -> Figure.createZType();
            case 3 -> Figure.createSymmetricZType();
            case 4 -> Figure.createBigCornerType();
            case 5 -> Figure.createTType();
            case 6 -> Figure.createLineType();
            case 7 -> Figure.createPointType();
            case 8 -> Figure.createSmallCornerType();
            case 9 -> Figure.createUnfinishedCrossroadsType();
            default -> throw new RuntimeException("No figure with such index");
        };
    }

    public static Figure create(FigureInfo info) {
        var figure = createFigureByIndex(info.getFigureIndex());
        for (int i = 0; i < info.getRotateIndex(); i++) {
            figure = figure.rotate();
        }
        return figure;
    }
}
