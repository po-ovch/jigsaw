package com.jigsaw.jigsaw.shared.messages;

import com.jigsaw.jigsaw.shared.Message;
import com.jigsaw.jigsaw.shared.MessageType;

public class FigureRequestMessage extends Message {
    public int playerIndex;

    public FigureRequestMessage(int playerIndex) {
        this.messageType = MessageType.FigureRequest;
        this.playerIndex = playerIndex;
    }
}
