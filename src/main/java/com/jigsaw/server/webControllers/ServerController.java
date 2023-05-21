package com.jigsaw.server.webControllers;

import com.jigsaw.shared.messages.FigureRequestMessage;
import com.jigsaw.shared.messages.GameStatisticsMessage;
import com.jigsaw.shared.messages.RegisterMessage;
import com.jigsaw.server.DbUtils;
import com.jigsaw.server.SharedComponents;
import jakarta.websocket.Session;

public class ServerController {

    public void registerNewPlayer(RegisterMessage message, Session session) {
        SharedComponents.manager.registerPlayer(message.playerName, session);
        if (SharedComponents.manager.checkGameStart()) {
            SharedComponents.manager.startGame();
        }
    }

    public void getFigure(FigureRequestMessage message) {
        var player = SharedComponents.manager.getPlayer(message.playerIndex);
        SharedComponents.manager.giveFigureToPlayer(player);
    }

    public void getStatistics(GameStatisticsMessage message) {
        DbUtils.saveResult(message.statistics);
        SharedComponents.manager.saveGameStatistics(message.statistics);
        if (SharedComponents.manager.checkAllPlayersFinished()) {
            SharedComponents.manager.defineWinner();
            SharedComponents.manager.readyForTheNextGame();
        }
    }
}
