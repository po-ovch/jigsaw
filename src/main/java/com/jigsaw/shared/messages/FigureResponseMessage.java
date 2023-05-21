package com.jigsaw.shared.messages;

import com.jigsaw.shared.Message;
import com.jigsaw.shared.MessageType;
import com.jigsaw.shared.entities.FigureInfo;

public class FigureResponseMessage extends Message {
    public FigureInfo figureInfo;

    public FigureResponseMessage(FigureInfo figureInfo) {
        this.messageType = MessageType.FigureResponse;
        this.figureInfo = figureInfo;
    }
}
