package com.jigsaw.jigsaw.shared.messages;

import com.jigsaw.jigsaw.shared.Message;
import com.jigsaw.jigsaw.shared.MessageType;
import com.jigsaw.jigsaw.shared.entities.GameStatistics;

public class GameStatisticsMessage extends Message {
    public GameStatistics statistics;

    public GameStatisticsMessage(GameStatistics statistics) {
        this.statistics = statistics;
        this.messageType = MessageType.GameStatistics;
    }
}
