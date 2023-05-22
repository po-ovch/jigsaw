package com.jigsaw.shared.messages;

import com.jigsaw.shared.Message;
import com.jigsaw.shared.MessageType;
import com.jigsaw.shared.entities.GameStatistics;

import java.util.List;

public class TopPlayersMessage extends Message {
    public List<GameStatistics> statistics;

    public TopPlayersMessage(List<GameStatistics> statistics) {
        messageType = MessageType.TopPlayers;
        this.statistics = statistics;
    }
}
