package com.jigsaw.jigsaw.client.webControllers;

import com.jigsaw.jigsaw.shared.messages.FigureRequestMessage;
import com.jigsaw.jigsaw.shared.messages.GameStatisticsMessage;
import com.jigsaw.jigsaw.shared.messages.RegisterMessage;
import com.jigsaw.jigsaw.shared.entities.GameStatistics;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;

public class ServerConnection {
    private Session session;
    private int playerIndex;

    public ServerConnection() {
        String server = "ws://localhost:7000/websockets/start";
        var client = ClientManager.createClient();
        try {
            this.session = client.connectToServer(JigsawClientEndpoint.class, new URI(server));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen() {
        if (session == null) {
            return false;
        }

        return session.isOpen();
    }

    public void registerPlayer(String playerName) {
        var message = new RegisterMessage(playerName);
        sendObjectSafely(message);
    }

    public void requestFigure() {
        var message = new FigureRequestMessage(playerIndex);
        sendObjectSafely(message);
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public void sendResult(GameStatistics gameStatistics) {
        var message = new GameStatisticsMessage(gameStatistics);
        sendObjectSafely(message);
    }

    private void sendObjectSafely(Object obj) {
        if (session == null) {
            System.out.println("Session is not created, cannot send");
            return;
        }
        try {
            session.getBasicRemote().sendObject(obj);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
}
