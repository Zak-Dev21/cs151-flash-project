package cs151.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles SQLite database connection and table creation.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:flashcards.db";

    /**
     * Creates a connection to the SQLite database.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Initializes database tables if they do not exist.
     */
    public static void initializeDatabase() {

        String createDeckTable = """
                CREATE TABLE IF NOT EXISTS decks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT,
                    color TEXT,
                    created_at TEXT NOT NULL
                );
                """;

        String createFlashcardTable = """
                CREATE TABLE IF NOT EXISTS flashcards (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    question TEXT NOT NULL,
                    answer TEXT NOT NULL,
                    deck_name TEXT NOT NULL,
                    created_at TEXT NOT NULL,
                    status TEXT NOT NULL,
                    last_reviewed_at TEXT
                );
                """;

        // Migrate existing flashcards table: add review columns if the DB predates them.
        String addStatus = "ALTER TABLE flashcards ADD COLUMN IF NOT EXISTS status TEXT NOT NULL DEFAULT 'New'";
        String addLastReviewed = "ALTER TABLE flashcards ADD COLUMN IF NOT EXISTS last_reviewed_at TEXT";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createDeckTable);
            statement.execute(createFlashcardTable);
            statement.execute(addStatus);
            statement.execute(addLastReviewed);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}