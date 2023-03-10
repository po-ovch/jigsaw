package com.jigsaw.jigsaw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Result implements Comparable<Result> {
    private final String playerName;
    private final Integer figuresMappedNum;
    private final String playedTime;
    private final String endTime;

    public Result(String[] arrayResult) {
        playerName = arrayResult[0];
        figuresMappedNum = Integer.parseInt(arrayResult[1]);
        playedTime = arrayResult[2];
        endTime = arrayResult[3];
    }

    public Result(String name, Integer figures, String gameTime, String endTime) {
        playerName = name;
        figuresMappedNum = figures;
        playedTime = gameTime;
        this.endTime = endTime;
    }

    @Override
    public int compareTo(Result o) {
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

    public static Result readResult(BufferedReader reader) throws IOException {
        String[] info = new String[4];
        for (int i = 0; i < 4; i++) {
            String str = reader.readLine();
            if (str.equals("problem")) {
                throw new RuntimeException();
            } else if (str.equals("end")) {
                return null;
            } else {
                info[i] = str;
            }
        }
        return new Result(info);
    }

    public void writeResult(BufferedWriter writer) throws IOException {
        writer.write(playerName + "\n");
        writer.write(figuresMappedNum + "\n");
        writer.write(playedTime + "\n");
        writer.write(endTime + "\n");
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
