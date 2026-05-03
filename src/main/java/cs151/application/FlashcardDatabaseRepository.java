package cs151.application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlashcardDatabaseRepository {

    /**
     * Saves a flashcard into the database.
     */
    public void saveFlashcard(Flashcard flashcard) throws SQLException {
        String sql = "INSERT INTO flashcards(question, answer, deck_name, created_at, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, flashcard.getQuestion());
            statement.setString(2, flashcard.getAnswer());
            statement.setString(3, flashcard.getDeckName());
            statement.setString(4, flashcard.getCreatedAt());
            statement.setString(5, flashcard.getStatus() != null ? flashcard.getStatus() : "New");

            statement.executeUpdate();
        }
    }

    /**
     * Retrieves all flashcards for a given deck.
     * Results are ordered by most recent first.
     */
    public List<Flashcard> getFlashcardsByDeck(String deckName) throws SQLException {
        List<Flashcard> flashcards = new ArrayList<>();

        String sql = "SELECT id, question, answer, deck_name, created_at, status, last_reviewed_at " +
                "FROM flashcards WHERE deck_name = ? ORDER BY created_at DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, deckName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    flashcards.add(mapRow(resultSet));
                }
            }
        }

        return flashcards;
    }

    /**
     * Retrieves all flashcards in the database.
     * Results are ordered by most recent first.
     */
    public List<Flashcard> getAllFlashcards() throws SQLException {
        List<Flashcard> flashcards = new ArrayList<>();

        String sql = "SELECT id, question, answer, deck_name, created_at, status, last_reviewed_at " +
                "FROM flashcards ORDER BY created_at DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                flashcards.add(mapRow(resultSet));
            }
        }

        return flashcards;
    }

    /**
     * Searches flashcards whose question or answer partially matches the given query.
     */
    public List<Flashcard> searchFlashcards(String query) throws SQLException {
        List<Flashcard> flashcards = new ArrayList<>();

        String sql = "SELECT id, question, answer, deck_name, created_at, status, last_reviewed_at " +
                "FROM flashcards WHERE question LIKE ? OR answer LIKE ? ORDER BY created_at DESC";

        String pattern = "%" + query + "%";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, pattern);
            statement.setString(2, pattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    flashcards.add(mapRow(resultSet));
                }
            }
        }

        return flashcards;
    }

    private Flashcard mapRow(ResultSet rs) throws SQLException {
        return new Flashcard(
                rs.getInt("id"),
                rs.getString("question"),
                rs.getString("answer"),
                rs.getString("deck_name"),
                rs.getString("created_at"),
                rs.getString("status"),
                rs.getString("last_reviewed_at")
        );
    }

    /**
     * Updates an existing flashcard's question, answer, and deck.
     */
    public void updateFlashcard(Flashcard flashcard) throws SQLException {
        String sql = "UPDATE flashcards SET question = ?, answer = ?, deck_name = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, flashcard.getQuestion());
            statement.setString(2, flashcard.getAnswer());
            statement.setString(3, flashcard.getDeckName());
            statement.setInt(4, flashcard.getId());

            statement.executeUpdate();
        }
    }

    /**
     * Deletes a flashcard by its database id.
     */
    public void deleteFlashcardById(int id) throws SQLException {
        String sql = "DELETE FROM flashcards WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void updateReviewStatus(Flashcard flashcard) throws SQLException {
        String sql = "UPDATE flashcards SET status = ?, last_reviewed_at = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, flashcard.getStatus());
            statement.setString(2, flashcard.getLastReviewedAt());
            statement.setInt(3, flashcard.getId());
            statement.executeUpdate();
        }
    }
}