package com.jigsaw.shared.messages;

import com.jigsaw.shared.Message;
import com.jigsaw.shared.MessageType;

public class FigureRequestMessage extends Message {
    public int playerIndex;

    public FigureRequestMessage(int playerIndex) {
        this.messageType = MessageType.FigureRequest;
        this.playerIndex = playerIndex;
    }
}
