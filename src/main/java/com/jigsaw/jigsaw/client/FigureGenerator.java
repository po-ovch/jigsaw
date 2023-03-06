package com.jigsaw.jigsaw.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FigureGenerator {

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

    private static String getFigureInfo() throws IOException {
        String figureInfo = null;

        var in = JigsawApplication.clientSocket.getReader();
        var out = JigsawApplication.clientSocket.getWriter();

        out.write("figure\n");
        out.flush();
        figureInfo = in.readLine();
        return figureInfo;
    }

    public static Figure generate() throws IOException {
        var figureInfo = getFigureInfo();

        List<Integer> indices;

        try {
            indices = Arrays.stream(figureInfo.split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        } catch (NumberFormatException formatException) {
            throw new PartnerDisconnectedException();
        }

        var figure = createFigureByIndex(indices.get(0));
        for (int i = 0; i < indices.get(1); i++) {
            figure = figure.rotate();
        }
        return figure;
    }
}
