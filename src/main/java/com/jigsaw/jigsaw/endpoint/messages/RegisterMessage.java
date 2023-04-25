package com.jigsaw.jigsaw.endpoint.messages;

import com.jigsaw.jigsaw.endpoint.Message;
import com.jigsaw.jigsaw.endpoint.MessageType;

public class RegisterMessage extends Message {
    public String playerName;

    public RegisterMessage(String playerName) {
        this.messageType = MessageType.Register;
        this.playerName = playerName;
    }

    public RegisterMessage() {
    }
}
