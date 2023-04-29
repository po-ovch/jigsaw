package com.jigsaw.jigsaw.shared.messages;

import com.jigsaw.jigsaw.shared.Message;
import com.jigsaw.jigsaw.shared.MessageType;
import com.jigsaw.jigsaw.shared.entities.Result;

public class ResultMessage extends Message {
    public Result result;

    public ResultMessage(Result result) {
        this.messageType = MessageType.Result;
        this.result = result;
    }
}
