package com.jigsaw.jigsaw.client.webControllers;

import com.jigsaw.jigsaw.client.SharedComponents;
import com.jigsaw.jigsaw.shared.Message;
import com.jigsaw.jigsaw.shared.MessageDecoder;
import com.jigsaw.jigsaw.shared.MessageEncoder;
import com.jigsaw.jigsaw.shared.MessageType;
import com.jigsaw.jigsaw.shared.messages.FigureResponseMessage;
import com.jigsaw.jigsaw.shared.messages.GameStatusMessage;
import com.jigsaw.jigsaw.shared.messages.ResultMessage;
import com.jigsaw.jigsaw.shared.entities.GameStatus;
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