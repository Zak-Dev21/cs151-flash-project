package cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Controller for the Define Deck page.
 * Handles user input and saves deck data into SQLite.
 */
public class DefineDeckController {

    @FXML
    private TextField deckNameField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ColorPicker deckColorPicker;

    @FXML
    private Label statusLabel;

    // Uses SQLite repository instead of flat-file storage
    private final DeckDatabaseRepository repository = new DeckDatabaseRepository();

    /**
     * Saves a new deck entered by the user.
     * A timestamp is added so decks can later be sorted by most recent first.
     */
    @FXML
    public void handleSaveDeck() {
        String deckName = deckNameField.getText().trim();
        String description = descriptionArea.getText().trim();
        String color = deckColorPicker.getValue().toString();

        // Deck name is required before saving
        if (deckName.isEmpty()) {
            statusLabel.setText("Deck name is required.");
            return;
        }

        // Store when the deck was created
        String createdAt = LocalDateTime.now().toString();

        Deck deck = new Deck(deckName, description, color, createdAt);

        try {
            repository.saveDeck(deck);
            statusLabel.setText("Deck saved successfully.");

            // Clear input fields after successful save
            deckNameField.clear();
            descriptionArea.clear();
        } catch (SQLException e) {
            statusLabel.setText("Error saving deck.");
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
