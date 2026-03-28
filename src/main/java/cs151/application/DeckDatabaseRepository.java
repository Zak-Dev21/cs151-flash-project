package cs151.application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading Deck data using SQLite.
 */
public class DeckDatabaseRepository {

    /**
     * Saves a deck into the database.
     */
    public void saveDeck(Deck deck) throws SQLException {
        String sql = "INSERT INTO decks(name, description, color, created_at) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, deck.getName());
            statement.setString(2, deck.getDescription());
            statement.setString(3, deck.getColor());
            statement.setString(4, deck.getCreatedAt());

            statement.executeUpdate();
        }
    }

    /**
     * Retrieves all decks sorted by newest first.
     */
    public List<Deck> getAllDecks() throws SQLException {
        List<Deck> decks = new ArrayList<>();

        // ORDER BY created_at DESC ensures most recent first
        String sql = "SELECT name, description, color, created_at FROM decks ORDER BY created_at DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Deck deck = new Deck(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("color"),
                        resultSet.getString("created_at")
                );
                decks.add(deck);
            }
        }

        return decks;
    }
}