package com.jigsaw.server;

import com.jigsaw.shared.messages.FigureResponseMessage;
import com.jigsaw.shared.messages.GameStatusMessage;
import com.jigsaw.shared.messages.ResultMessage;
import com.jigsaw.shared.entities.FigureInfo;
import com.jigsaw.shared.entities.GameSettings;
import com.jigsaw.shared.entities.GameStatus;
import com.jigsaw.shared.entities.Result;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Session;

import java.io.IOException;

public class Player {
    private final Session session;
    public int gotFiguresNumber;
    public String playerName;
    public int playerIndex;

    public Player(String playerName, int playerIndex, Session session) {
        this.playerName = playerName;
        this.session = session;
        this.gotFiguresNumber = 0;
        this.playerIndex = playerIndex;
    }

    public void waitForStart() {
        var message = new GameStatusMessage(playerName, playerIndex, GameStatus.Wait, null, null);
        sendObjectSafely(message);
    }

    public void startGame(GameSettings settings, FigureInfo firstFigure) {
        var message = new GameStatusMessage(playerName, playerIndex, GameStatus.Play, settings, firstFigure);
        sendObjectSafely(message);
    }

    public void giveFigure(FigureInfo figureInfo) {
        this.gotFiguresNumber++;
        var message = new FigureResponseMessage(figureInfo);
        sendObjectSafely(message);
    }

    public void notifyAboutResults(Result result) {
        var message = new ResultMessage(result);
        sendObjectSafely(message);
    }

    private void sendObjectSafely(Object obj) {
        try {
            session.getBasicRemote().sendObject(obj);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
}
