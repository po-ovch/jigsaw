package com.jigsaw.jigsaw.shared.messages;

import com.jigsaw.jigsaw.shared.Message;
import com.jigsaw.jigsaw.shared.MessageType;
import com.jigsaw.jigsaw.shared.entities.FigureInfo;

public class FigureResponseMessage extends Message {
    public FigureInfo figureInfo;

    public FigureResponseMessage(FigureInfo figureInfo) {
        this.messageType = MessageType.FigureResponse;
        this.figureInfo = figureInfo;
    }
}
