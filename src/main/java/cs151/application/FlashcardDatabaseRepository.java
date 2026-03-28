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

        String sql = "SELECT question, answer, deck_name, created_at " +
                "FROM flashcards WHERE deck_name = ? ORDER BY created_at DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, deckName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Flashcard flashcard = new Flashcard(
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
     * Results are ordered by most recent first so the newest
     * flashcards appear at the top of the TableView.
     */
    public List<Flashcard> getAllFlashcards() throws SQLException {
        List<Flashcard> flashcards = new ArrayList<>();

        String sql = "SELECT question, answer, deck_name, created_at " +
                "FROM flashcards ORDER BY created_at DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Flashcard flashcard = new Flashcard(
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
}