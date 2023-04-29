package com.jigsaw.jigsaw.shared.messages;

import com.jigsaw.jigsaw.shared.Message;
import com.jigsaw.jigsaw.shared.MessageType;

public class RegisterMessage extends Message {
    public String playerName;

    public RegisterMessage(String playerName) {
        this.messageType = MessageType.Register;
        this.playerName = playerName;
    }

    public RegisterMessage() {
    }
}
