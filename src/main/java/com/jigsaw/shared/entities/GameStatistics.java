package com.jigsaw.shared.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GameStatistics implements Serializable, Comparable<GameStatistics> {
    private final String playerName;
    private final Integer figuresMappedNum;
    private final String playedTime;
    private final String endTime;

    public GameStatistics(String[] arrayResult) {
        playerName = arrayResult[0];
        figuresMappedNum = Integer.parseInt(arrayResult[1]);
        playedTime = arrayResult[2];
        endTime = arrayResult[3];
    }

    public GameStatistics(String name, Integer figures, String gameTime, String endTime) {
        playerName = name;
        figuresMappedNum = figures;
        playedTime = gameTime;
        this.endTime = endTime;
    }

    @Override
    public int compareTo(GameStatistics o) {
        if (figuresMappedNum.equals(o.figuresMappedNum)) {
            return playedTime.compareTo(o.playedTime);
        }
        return figuresMappedNum.compareTo(o.figuresMappedNum);
    }

    @Override
    public String toString() {
        return playerName + "\n" +
               figuresMappedNum + "\n" +
               playedTime + "\n" +
               endTime + "\n";
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayedTime() {
        return playedTime;
    }

    public Integer getFiguresMappedNum() {
        return figuresMappedNum;
    }

    public String getEndTime() {
        return endTime;
    }

    public Double getPlayedTimeDouble() {
        return Double.parseDouble(playedTime);
    }

    public LocalDateTime getEndTimeDt() {
        return LocalDateTime.parse(endTime);
    }
}
