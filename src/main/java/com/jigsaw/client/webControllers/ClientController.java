package com.jigsaw.client.webControllers;

import com.jigsaw.client.SharedComponents;
import com.jigsaw.client.viewControllers.GameController;
import com.jigsaw.client.viewControllers.HelloController;
import com.jigsaw.shared.entities.FigureInfo;
import com.jigsaw.shared.entities.GameSettings;
import com.jigsaw.shared.entities.Result;

public class ClientController {
    public void waitForOtherPlayers() {
        HelloController.awaitGame();
    }

    public void readyToStartGame(GameSettings settings, FigureInfo figureInfo) {
        SharedComponents.settings = settings;
        SharedComponents.firstFigure = figureInfo;
        HelloController.startGame();
    }

    public void gotFigure(FigureInfo newFigure) {
        GameController.setFigure(newFigure);
    }

    public void gotResult(Result result) {
        HelloController.showResults(result);
    }
}
