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
        String sql = "INSERT INTO flashcards(question, answer, deck_name, created_at) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, flashcard.getQuestion());
            statement.setString(2, flashcard.getAnswer());
            statement.setString(3, flashcard.getDeckName());
            statement.setString(4, flashcard.getCreatedAt());

            statement.executeUpdate();
        }
    }

    /**
     * Retrieves all flashcards for a given deck.
     * Results are ordered by most recent first.
     */
    public List<Flashcard> getFlashcardsByDeck(String deckName) throws SQLException {
        List<Flashcard> flashcards = new ArrayList<>();

        String sql = "SELECT id, question, answer, deck_name, created_at " +
                "FROM flashcards WHERE deck_name = ? ORDER BY created_at DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, deckName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Flashcard flashcard = new Flashcard(
                            resultSet.getInt("id"),
                            resultSet.getString("question"),
                            resultSet.getString("answer"),
                            resultSet.getString("deck_name"),
                            resultSet.getString("created_at")
                    );
                    flashcards.add(flashcard);
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

        String sql = "SELECT id, question, answer, deck_name, created_at " +
                "FROM flashcards ORDER BY created_at DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Flashcard flashcard = new Flashcard(
                        resultSet.getInt("id"),
                        resultSet.getString("question"),
                        resultSet.getString("answer"),
                        resultSet.getString("deck_name"),
                        resultSet.getString("created_at")
                );
                flashcards.add(flashcard);
            }
        }

        return flashcards;
    }

    /**
     * Searches flashcards whose question or answer partially matches the given query.
     */
    public List<Flashcard> searchFlashcards(String query) throws SQLException {
        List<Flashcard> flashcards = new ArrayList<>();

        String sql = "SELECT id, question, answer, deck_name, created_at " +
                "FROM flashcards WHERE question LIKE ? OR answer LIKE ? ORDER BY created_at DESC";

        String pattern = "%" + query + "%";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, pattern);
            statement.setString(2, pattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Flashcard flashcard = new Flashcard(
                            resultSet.getInt("id"),
                            resultSet.getString("question"),
                            resultSet.getString("answer"),
                            resultSet.getString("deck_name"),
                            resultSet.getString("created_at")
                    );
                    flashcards.add(flashcard);
                }
            }
        }

        return flashcards;
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
}