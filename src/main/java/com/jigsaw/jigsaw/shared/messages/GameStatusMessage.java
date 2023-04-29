package com.jigsaw.jigsaw.shared.messages;

import com.jigsaw.jigsaw.shared.MessageType;
import com.jigsaw.jigsaw.shared.entites.FigureInfo;
import com.jigsaw.jigsaw.shared.entites.GameSettings;
import com.jigsaw.jigsaw.shared.entites.GameStatus;
import com.jigsaw.jigsaw.shared.Message;

public class GameStatusMessage extends Message {
    public String playerName;
    public int playerIndex;
    public GameStatus status;
    public GameSettings settings;
    public FigureInfo firstFigure;

    public GameStatusMessage(String playerName,
                             int playerIndex,
                             GameStatus status,
                             GameSettings settings,
                             FigureInfo firstFigure) {
        if (status == GameStatus.Play && settings == null) {
            throw new IllegalArgumentException();
        }

        this.playerName = playerName;
        this.playerIndex = playerIndex;
        this.status = status;
        this.settings = settings;
        this.messageType = MessageType.GameStatus;
        this.firstFigure = firstFigure;
    }
}
