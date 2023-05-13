package com.jigsaw.jigsaw.client.web;

import com.jigsaw.jigsaw.endpoint.messages.FigureRequestMessage;
import com.jigsaw.jigsaw.endpoint.messages.GameStatisticsMessage;
import com.jigsaw.jigsaw.endpoint.messages.RegisterMessage;
import com.jigsaw.jigsaw.endpoint.shared.GameStatistics;
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

    public void registerPlayer(String playerName) {
        var message  = new RegisterMessage(playerName);
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
        System.out.println("HUI2");
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
