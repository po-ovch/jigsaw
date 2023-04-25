package com.jigsaw.jigsaw.endpoint.messages;

import com.jigsaw.jigsaw.endpoint.Message;
import com.jigsaw.jigsaw.endpoint.MessageType;
import com.jigsaw.jigsaw.endpoint.shared.Result;

public class ResultMessage extends Message {
    public Result result;

    public ResultMessage(Result result) {
        this.messageType = MessageType.Result;
        this.result = result;
    }
}
