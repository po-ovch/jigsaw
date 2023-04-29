package com.jigsaw.jigsaw.server;

import com.jigsaw.jigsaw.shared.entites.GameStatistics;
import com.jigsaw.jigsaw.server.dao.ResultDAO;
import com.jigsaw.jigsaw.server.db.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DbUtils {
    private static final ResultDAO result = new ResultDAO();

    private static List<GameStatistics> getResults() {
        try {
            var results = result.findAll();
            List<GameStatistics> stats = new ArrayList<>();
            for (Result res : results) {
                stats.add(new GameStatistics(res.getPlayerName(), res.getFiguresMappedNum(), res.getPlayedTime(), res.getEndTime()));
            }

            return stats;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<GameStatistics> getTop() {
        var results = getResults();
        if (results == null) {
            return null;
        }
        results.sort(Comparator.comparing(GameStatistics::getEndTimeDt));
        results.sort(Comparator.comparing(GameStatistics::getPlayedTimeDouble));
        results.sort(Comparator.comparing(GameStatistics::getFiguresMappedNum));
        Collections.reverse(results);
        return results.stream().limit(10).toList();
    }

    public static void saveResult(GameStatistics gameStatistics) {

        try {
            result.save(new Result(gameStatistics.getPlayerName(), gameStatistics.getFiguresMappedNum(), gameStatistics.getPlayedTime(), gameStatistics.getEndTime()));
        } catch (Throwable e) {
            System.out.println("log: unsuccessful save of result");
        }
    }
}
