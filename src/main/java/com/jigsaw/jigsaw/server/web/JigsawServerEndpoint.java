package com.jigsaw.jigsaw.server.web;

import com.jigsaw.jigsaw.shared.Message;
import com.jigsaw.jigsaw.shared.MessageDecoder;
import com.jigsaw.jigsaw.shared.MessageEncoder;
import com.jigsaw.jigsaw.shared.MessageType;
import com.jigsaw.jigsaw.shared.messages.FigureRequestMessage;
import com.jigsaw.jigsaw.shared.messages.GameStatisticsMessage;
import com.jigsaw.jigsaw.shared.messages.RegisterMessage;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/start", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class JigsawServerEndpoint {
    private final ServerController serverController = new ServerController();

    @OnMessage
    public void communicate(Message message, Session session) {
        if (message.messageType == MessageType.Register) {
            serverController.registerNewPlayer((RegisterMessage)message, session);
        } else if (message.messageType == MessageType.FigureRequest) {
            serverController.getFigure((FigureRequestMessage) message);
        } else if (message.messageType == MessageType.GameStatistics) {
            serverController.getStatistics((GameStatisticsMessage) message);
        }
    }
}
