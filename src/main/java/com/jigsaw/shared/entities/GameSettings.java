package com.jigsaw.shared.entities;

import java.io.Serializable;

public class GameSettings implements Serializable {
    public String playerName;
    public boolean isMultiplayer;
    public String rivalName;
    public int gameDuration;

    public GameSettings(String playerName, boolean isMultiplayer, String rivalName,
                        int gameDuration) {
        this.playerName = playerName;
        this.isMultiplayer = isMultiplayer;
        this.rivalName = rivalName;
        this.gameDuration = gameDuration;
    }
}
