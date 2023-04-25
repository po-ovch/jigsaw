package com.jigsaw.jigsaw.endpoint.messages;

import com.jigsaw.jigsaw.endpoint.Message;
import com.jigsaw.jigsaw.endpoint.MessageType;
import com.jigsaw.jigsaw.endpoint.shared.FigureInfo;

public class FigureResponseMessage extends Message {
    public FigureInfo figureInfo;

    public FigureResponseMessage(FigureInfo figureInfo) {
        this.messageType = MessageType.FigureResponse;
        this.figureInfo = figureInfo;
    }
}
