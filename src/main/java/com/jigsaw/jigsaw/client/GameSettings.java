package com.jigsaw.jigsaw.client;

public class GameSettings {
    public String playerName;
    public boolean isMultiplayer;
    public String rivalName;
    public int gameDuration;

    public GameSettings(String playerName, String isMultiplayer, String rivalName,
                        int gameDuration) {
        this.playerName = playerName;
        this.isMultiplayer = isMultiplayer.equals("true");
        this.rivalName = rivalName;
        this.gameDuration = gameDuration;
    }
}
