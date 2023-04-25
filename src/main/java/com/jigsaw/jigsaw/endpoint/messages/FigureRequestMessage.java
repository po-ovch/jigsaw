package com.jigsaw.jigsaw.endpoint.messages;

import com.jigsaw.jigsaw.endpoint.Message;
import com.jigsaw.jigsaw.endpoint.MessageType;

public class FigureRequestMessage extends Message {
    public int playerIndex;

    public FigureRequestMessage(int playerIndex) {
        this.messageType = MessageType.FigureRequest;
        this.playerIndex = playerIndex;
    }
}
