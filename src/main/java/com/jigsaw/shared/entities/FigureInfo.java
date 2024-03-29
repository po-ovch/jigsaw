package com.jigsaw.shared.entities;

import java.io.Serializable;

public record FigureInfo(int figureIndex, int rotateIndex) implements Serializable {

    public int getFigureIndex() {
        return figureIndex;
    }

    public int getRotateIndex() {
        return rotateIndex;
    }
}
