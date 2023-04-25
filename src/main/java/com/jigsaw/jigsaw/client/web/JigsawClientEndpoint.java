package com.jigsaw.jigsaw.client.web;

import com.jigsaw.jigsaw.client.SharedComponents;
import com.jigsaw.jigsaw.client.viewControllers.HelloController;
import com.jigsaw.jigsaw.endpoint.Message;
import com.jigsaw.jigsaw.endpoint.MessageDecoder;
import com.jigsaw.jigsaw.endpoint.MessageEncoder;
import com.jigsaw.jigsaw.endpoint.MessageType;
import com.jigsaw.jigsaw.endpoint.messages.FigureResponseMessage;
import com.jigsaw.jigsaw.endpoint.messages.GameStatusMessage;
import com.jigsaw.jigsaw.endpoint.messages.ResultMessage;
import com.jigsaw.jigsaw.endpoint.shared.GameStatus;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;

@ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class JigsawClientEndpoint {
    private final ClientController clientController = new ClientController();

    @OnMessage
    public void onMessage(Message message) {
        if (message.messageType == MessageType.GameStatus) {
            var gameStatusMessage = (GameStatusMessage)message;
            SharedComponents.server.setPlayerIndex(gameStatusMessage.playerIndex);
            if (gameStatusMessage.status == GameStatus.Wait) {
                clientController.waitForOtherPlayers();
            } else if (gameStatusMessage.status == GameStatus.Play) {
                clientController.readyToStartGame(gameStatusMessage.settings, gameStatusMessage.firstFigure);
            }
        } else if (message.messageType == MessageType.FigureResponse) {
            var figureResponseMessage = (FigureResponseMessage)message;
            clientController.gotFigure(figureResponseMessage.figureInfo);
        } else if (message.messageType == MessageType.Result) {
            var resultMessage = (ResultMessage)message;
            clientController.gotResult(resultMessage.result);
        }
    }
}
