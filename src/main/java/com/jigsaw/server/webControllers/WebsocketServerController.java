package com.jigsaw.server.webControllers;

import com.jigsaw.server.dao.ResultDAO;
import com.jigsaw.server.db.Result;
import com.jigsaw.shared.messages.FigureRequestMessage;
import com.jigsaw.shared.messages.GameStatisticsMessage;
import com.jigsaw.shared.messages.RegisterMessage;
import com.jigsaw.server.SharedComponents;
import jakarta.websocket.Session;

public class WebsocketServerController {
    private final ResultDAO results = new ResultDAO();

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
        var r = new Result();
        r.setEndTime(message.statistics.getEndTime());
        r.setPlayedTime(message.statistics.getPlayedTime());
        r.setPlayerName(message.statistics.getPlayerName());
        r.setFiguresMappedNum(message.statistics.getFiguresMappedNum());
        results.save(r);
        SharedComponents.manager.saveGameStatistics(message.statistics);
        if (SharedComponents.manager.checkAllPlayersFinished()) {
            SharedComponents.manager.defineWinner();
            SharedComponents.manager.readyForTheNextGame();
        }
    }
}
