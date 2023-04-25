package com.jigsaw.jigsaw.endpoint.messages;

import com.jigsaw.jigsaw.endpoint.Message;
import com.jigsaw.jigsaw.endpoint.MessageType;
import com.jigsaw.jigsaw.endpoint.shared.GameStatistics;

public class GameStatisticsMessage extends Message {
    public GameStatistics statistics;

    public GameStatisticsMessage(GameStatistics statistics) {
        this.statistics = statistics;
        this.messageType = MessageType.GameStatistics;
    }
}
