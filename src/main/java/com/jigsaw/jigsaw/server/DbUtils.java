package com.jigsaw.jigsaw.server;

import com.jigsaw.jigsaw.endpoint.shared.GameStatistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DbUtils {
    private static final String CREATE_TABLE_STRING = "CREATE TABLE RESULTS_LIST  "
            + "(ID INT NOT NULL GENERATED ALWAYS AS IDENTITY "
            + "   CONSTRAINT RESULT_PK PRIMARY KEY, "
            + "LOGIN VARCHAR(10) NOT NULL, "
            + "END_TIME TIMESTAMP NOT NULL, "
            + "MOVES_MADE INT NOT NULL, "
            + "TIME_SPENT DOUBLE NOT NULL) ";

    private static boolean checkTable(Connection conn) throws SQLException {
        try {
            Statement statement = conn.createStatement();
            statement.executeQuery("select LOGIN from RESULTS_LIST");
        } catch (SQLException sqle) {
            String theError = (sqle).getSQLState();
            if (theError.equals("42X05"))
            {
                return false;
            } else if (theError.equals("42X14") || theError.equals("42821")) {
                conn.createStatement().execute("drop RESULTS_LIST");
                return false;
            } else {
                throw sqle;
            }
        }
        return true;
    }

    private static List<GameStatistics> getResults() {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String dbName = "resultsDB";
        String connectionURL = "jdbc:derby:.\\DERBY\\" + dbName + ";create=true";

        try {
            Connection conn = DriverManager.getConnection(connectionURL);
            var statement = conn.createStatement();
            if (!DbUtils.checkTable(conn)) {
                statement.execute(CREATE_TABLE_STRING);
            }

            var resultsFromDb = statement.executeQuery("select * from RESULTS_LIST");
            var results = new ArrayList<GameStatistics>();
            while (resultsFromDb.next()) {
                var result = new GameStatistics(resultsFromDb.getString("LOGIN"), resultsFromDb.getInt("MOVES_MADE"),
                        resultsFromDb.getString("TIME_SPENT"), resultsFromDb.getString("END_TIME"));
                results.add(result);
            }
            return results;
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
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String dbName = "resultsDB";
        String connectionURL = "jdbc:derby:.\\DERBY\\" + dbName + ";create=true";

        try {
            var conn = DriverManager.getConnection(connectionURL);
            var statement = conn.createStatement();
            if (!DbUtils.checkTable(conn)) {
                statement.execute(CREATE_TABLE_STRING);
            }

            var preparedStatement = conn.prepareStatement("insert into RESULTS_LIST(LOGIN, END_TIME, MOVES_MADE, TIME_SPENT) " +
                    "values (?, ?, ?, ?)");
            preparedStatement.setString(1, gameStatistics.getPlayerName());
            preparedStatement.setString(2, gameStatistics.getEndTime());
            preparedStatement.setInt(3, gameStatistics.getFiguresMappedNum());
            preparedStatement.setString(4, gameStatistics.getPlayedTime());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            statement.close();
            conn.close();
        } catch (Throwable e) {
            System.out.println("log: unsuccessful save of result");
        }
    }

    public static void closeServer() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException se) {
            // ignore
        }
    }
}
