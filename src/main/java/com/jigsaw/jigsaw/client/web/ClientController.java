package com.jigsaw.jigsaw.client.web;

import com.jigsaw.jigsaw.client.SharedComponents;
import com.jigsaw.jigsaw.client.viewControllers.GameController;
import com.jigsaw.jigsaw.client.viewControllers.HelloController;
import com.jigsaw.jigsaw.endpoint.shared.FigureInfo;
import com.jigsaw.jigsaw.endpoint.shared.GameSettings;
import com.jigsaw.jigsaw.endpoint.shared.Result;

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
        System.out.println("HHHHH");
        HelloController.showResults(result);
    }
}
