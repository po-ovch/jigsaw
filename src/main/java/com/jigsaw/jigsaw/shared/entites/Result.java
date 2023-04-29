package com.jigsaw.jigsaw.shared.entites;

import java.io.Serializable;

public class Result implements Serializable {
    public String winnerName;
    public String playerName;
    public String overallTime;

    public Result(String winnerName, String playerName, String overallTime) {
        this.winnerName = winnerName;
        this.overallTime = overallTime;
        this.playerName = playerName;
    }
}
