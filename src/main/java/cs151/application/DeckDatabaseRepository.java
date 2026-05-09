package cs151.application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeckDatabaseRepository implements DeckRepository {

    @Override
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

    @Override
    public List<Deck> getAllDecks() throws SQLException {
        List<Deck> decks = new ArrayList<>();

        String sql = "SELECT id, name, description, color, created_at FROM decks ORDER BY created_at DESC";

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
                deck.setId(resultSet.getInt("id"));
                decks.add(deck);
            }
        }

        return decks;
    }

    public void updateDeck(Deck deck) throws SQLException {
        String sql = "UPDATE decks SET name = ?, description = ?, color = ? WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, deck.getName());
            statement.setString(2, deck.getDescription());
            statement.setString(3, deck.getColor());
            statement.setInt(4, deck.getId());

            statement.executeUpdate();
        }
    }

    public void deleteDeck(int id) throws SQLException {
        String deleteDeckSql = "DELETE FROM decks WHERE id = ?";
        String deleteFlashcardsSql = "DELETE FROM flashcards WHERE deck_name = (SELECT name FROM decks WHERE id = ?)";

        try (Connection connection = DatabaseManager.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement ps = connection.prepareStatement(deleteFlashcardsSql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = connection.prepareStatement(deleteDeckSql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }
}
