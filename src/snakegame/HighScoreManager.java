package snakegame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScoreManager {

    private final PreparedStatement insertStatement;
    private final PreparedStatement deleteStatement;
    private final Connection connection;

    public HighScoreManager() throws SQLException {

        String dbURL = "jdbc:mysql://localhost:3306/highscores?createDatabaseIfNotExist=true&serverTimezone=UTC";
        connection = DriverManager.getConnection(dbURL, "root", "root");

        String insertQuery = "INSERT INTO HIGHSCORES (TIMESTAMP, NAME, SCORE) VALUES (?, ?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);

        String deleteQuery = "DELETE FROM HIGHSCORES WHERE SCORE=?";
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

    public List<HighScore> getSortedHighScores() throws SQLException {
        String query = "SELECT * FROM HIGHSCORES";
        List<HighScore> highScores = new ArrayList<>();

        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);

        while (results.next()) {
            String name = results.getString("NAME");
            int score = results.getInt("SCORE");
            highScores.add(new HighScore(name, score));
        }

        sortHighScores(highScores);
        return highScores;
    }

    public void putHighScore(String name, int score) throws SQLException {
        List<HighScore> highScores = getSortedHighScores();
        insertScore(name, score);
    }

    /**
     * Sort the high scores in descending order.
     *
     * @param highScores
     */
    private void sortHighScores(List<HighScore> highScores) {
        Collections.sort(highScores, Comparator.comparing(HighScore::getScore).reversed());
    }

    private void insertScore(String name, int score) throws SQLException {
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        insertStatement.setTimestamp(1, ts);
        insertStatement.setString(2, name);
        insertStatement.setInt(3, score);

        insertStatement.executeUpdate();
    }

    /**
     * Deletes all the highscores with score.
     *
     * @param score
     */
    private void deleteScores(int score) throws SQLException {
        deleteStatement.setInt(1, score);
        deleteStatement.executeUpdate();
    }
}
