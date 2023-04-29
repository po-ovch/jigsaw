package com.jigsaw.jigsaw.server.web;

import com.jigsaw.jigsaw.shared.messages.FigureRequestMessage;
import com.jigsaw.jigsaw.shared.messages.GameStatisticsMessage;
import com.jigsaw.jigsaw.shared.messages.RegisterMessage;
import com.jigsaw.jigsaw.server.DbUtils;
import jakarta.websocket.Session;

import static com.jigsaw.jigsaw.server.SharedComponents.manager;

public class ServerController {

    public void registerNewPlayer(RegisterMessage message, Session session) {
        manager.registerPlayer(message.playerName, session);
        if (manager.checkGameStart()) {
            manager.startGame();
        }
    }

    public void getFigure(FigureRequestMessage message) {
        var player = manager.getPlayer(message.playerIndex);
        manager.giveFigureToPlayer(player);
    }

    public void getStatistics(GameStatisticsMessage message) {
        DbUtils.saveResult(message.statistics);
        manager.saveGameStatistics(message.statistics);
        if (manager.checkAllPlayersFinished()) {
            manager.defineWinner();
            manager.readyForTheNextGame();
        }
    }
}
