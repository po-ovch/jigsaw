package com.jigsaw.jigsaw.server;

import com.jigsaw.jigsaw.Result;

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
    // master push again

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

    private static List<Result> getResults() {
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
            var results = new ArrayList<Result>();
            while (resultsFromDb.next()) {
                var result = new Result(resultsFromDb.getString("LOGIN"), resultsFromDb.getInt("MOVES_MADE"),
                        resultsFromDb.getString("TIME_SPENT"), resultsFromDb.getString("END_TIME"));
                results.add(result);
            }
            return results;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Result> getTop() {
        var results = getResults();
        if (results == null) {
            return null;
        }
        results.sort(Comparator.comparing(Result::getEndTimeDt));
        results.sort(Comparator.comparing(Result::getPlayedTimeDouble));
        results.sort(Comparator.comparing(Result::getFiguresMappedNum));
        Collections.reverse(results);
        return results.stream().limit(10).toList();
    }

    public static void saveResult(Result result) {
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
            preparedStatement.setString(1, result.getPlayerName());
            preparedStatement.setString(2, result.getEndTime());
            preparedStatement.setInt(3, result.getFiguresMappedNum());
            preparedStatement.setString(4, result.getPlayedTime());

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
