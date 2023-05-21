package com.jigsaw.client.webControllers;

import com.jigsaw.client.SharedComponents;
import com.jigsaw.client.viewControllers.GameController;
import com.jigsaw.shared.entities.FigureInfo;
import com.jigsaw.shared.entities.GameSettings;
import com.jigsaw.shared.entities.Result;

import static com.jigsaw.client.SharedComponents.viewManager;

public class ClientController {
    public void waitForOtherPlayers() {
        viewManager.awaitPlayers();
    }

    public void readyToStartGame(GameSettings settings, FigureInfo figureInfo) {
        SharedComponents.settings = settings;
        SharedComponents.firstFigure = figureInfo;
        viewManager.readyToStart();
    }

    public void gotFigure(FigureInfo newFigure) {
        GameController.setFigure(newFigure);
    }

    public void gotResult(Result result) {
        SharedComponents.result = result;
        viewManager.showResults();
    }
}
