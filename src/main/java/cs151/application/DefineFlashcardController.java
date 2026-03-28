package cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for the Define Flashcard page.
 * Lets the user enter a question/answer, choose a deck,
 * and save the flashcard into SQLite.
 */
public class DefineFlashcardController {

    @FXML
    private TextField questionField;

    @FXML
    private TextField answerField;

    @FXML
    private ComboBox<String> deckComboBox;

    @FXML
    private Label statusLabel;

    // Repositories used to load deck names and save flashcards
    private final DeckDatabaseRepository deckRepository = new DeckDatabaseRepository();
    private final FlashcardDatabaseRepository flashcardRepository = new FlashcardDatabaseRepository();

    /**
     * Runs automatically when the page loads.
     * Loads existing deck names into the dropdown.
     */
    @FXML
    public void initialize() {
        try {
            List<Deck> decks = deckRepository.getAllDecks();

            for (Deck deck : decks) {
                deckComboBox.getItems().add(deck.getName());
            }

            // Helpful message if no deck exists yet
            if (decks.isEmpty()) {
                statusLabel.setText("Create a deck first before adding flashcards.");
            }
        } catch (SQLException e) {
            statusLabel.setText("Error loading decks.");
            e.printStackTrace();
        }
    }

    /**
     * Saves a flashcard to the database.
     * All fields must be filled in before saving.
     */
    @FXML
    public void handleSaveFlashcard() {
        String question = questionField.getText().trim();
        String answer = answerField.getText().trim();
        String deckName = deckComboBox.getValue();

        // Basic validation
        if (question.isEmpty() || answer.isEmpty() || deckName == null) {
            statusLabel.setText("Question, answer, and deck are required.");
            return;
        }

        // Store creation time so list page can sort newest first
        String createdAt = LocalDateTime.now().toString();

        Flashcard flashcard = new Flashcard(question, answer, deckName, createdAt);

        try {
            flashcardRepository.saveFlashcard(flashcard);
            statusLabel.setText("Flashcard saved successfully.");

            // Clear only text inputs after a successful save
            questionField.clear();
            answerField.clear();
            deckComboBox.setValue(null);
        } catch (SQLException e) {
            statusLabel.setText("Error saving flashcard.");
            e.printStackTrace();
        }
    }

    /**
     * Returns the user to the home page.
     */
    @FXML
    public void handleBackHome(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}