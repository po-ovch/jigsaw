package com.jigsaw.jigsaw.server.db;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "results")
public class Result {
    private String playerName;
    private Integer figuresMappedNum;
    private String playedTime;
    private String endTime;
    private Long id;

    public Result(String playerName, Integer figuresMappedNum, String playedTime, String endTime) {
        this.playerName = playerName;
        this.figuresMappedNum = figuresMappedNum;
        this.playedTime = playedTime;
        this.endTime = endTime;
    }

    public Result() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlayerName(String newPlayerName) {
        playerName = newPlayerName;
    }

    public void setPlayedTime(String newPlayedTime) {
        playedTime = newPlayedTime;
    }

    public void setFiguresMappedNum(Integer newFiguresMappedNum) {
        figuresMappedNum = newFiguresMappedNum;
    }

    public void setEndTime(String newEndTime) {
        endTime = newEndTime;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
}
