package com.jigsaw.server;

import com.jigsaw.shared.entities.*;
import jakarta.websocket.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameManager {
    private static final List<Player> players = new ArrayList<>();
    private final List<FigureInfo> figures = Collections.synchronizedList(new ArrayList<>());
    private final List<GameStatistics> gameStatistics = Collections.synchronizedList(new ArrayList<>());

    private int playersNum;
    private int gameDuration;

    public void setPlayersNum(int playersNum) {
       this.playersNum = playersNum;
    }

    public void setGameDuration(int gameDuration) {
        this.gameDuration = gameDuration;
    }

    public void registerPlayer(String playerName, Session session) {
        var player = new Player(playerName, players.size(), session);
        players.add(player);
        player.waitForStart();
    }

    public boolean checkGameStart() {
        return players.size() == playersNum;
    }

    public void startGame() {
        var firstFigure = generateFirstFigure();
        for (int i = 0; i < players.size(); i++) {
            var player = players.get(i);
            var rival = players.get(players.size() - i - 1);
            var settings = new GameSettings(player.playerName, players.size() == 2,
                    rival.playerName, gameDuration);
            player.startGame(settings, firstFigure);
        }
    }

    private FigureInfo generateFirstFigure() {
        var firstFigure = FigureGenerator.generate();
        figures.add(firstFigure);
        return firstFigure;
    }

    public void giveFigureToPlayer(Player player) {
        if (player.gotFiguresNumber >= figures.size()) {
            var newFigure = FigureGenerator.generate();
            figures.add(newFigure);
        }
        player.giveFigure(figures.get(figures.size() - 1));
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public void saveGameStatistics(GameStatistics statistics) {
        gameStatistics.add(statistics);
    }

    public boolean checkAllPlayersFinished() {
        return playersNum == gameStatistics.size();
    }

    public void defineWinner() {
        Collections.sort(gameStatistics);
        var winnerStatistics = gameStatistics.get(gameStatistics.size() - 1);
        for (var player : players) {
            String overallTime;
            if (winnerStatistics.getPlayerName().equals(player.playerName)) {
                overallTime = winnerStatistics.getPlayedTime();
            } else {
                overallTime = gameStatistics.get(0).getPlayedTime();
            }
            var result = new Result(winnerStatistics.getPlayerName(), player.playerName, overallTime);
            player.notifyAboutResults(result);
        }
    }

    public void readyForTheNextGame() {
        gameStatistics.clear();
    }
}
