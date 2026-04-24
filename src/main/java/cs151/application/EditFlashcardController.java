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
import java.util.List;

public class EditFlashcardController {

    @FXML private TextField questionField;
    @FXML private TextField answerField;
    @FXML private ComboBox<String> deckComboBox;
    @FXML private Label statusLabel;

    private Flashcard flashcard;

    private final FlashcardDatabaseRepository flashcardRepository = new FlashcardDatabaseRepository();
    private final DeckDatabaseRepository deckRepository = new DeckDatabaseRepository();

    @FXML
    public void initialize() {
        try {
            List<Deck> decks = deckRepository.getAllDecks();
            for (Deck deck : decks) {
                deckComboBox.getItems().add(deck.getName());
            }
        } catch (SQLException e) {
            statusLabel.setText("Error loading decks.");
        }
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
        questionField.setText(flashcard.getQuestion());
        answerField.setText(flashcard.getAnswer());
        deckComboBox.setValue(flashcard.getDeckName());
    }

    @FXML
    public void handleSave(ActionEvent actionEvent) throws IOException {
        String question = questionField.getText().trim();
        String answer = answerField.getText().trim();
        String deckName = deckComboBox.getValue();

        if (question.isEmpty() || answer.isEmpty() || deckName == null) {
            statusLabel.setText("All fields are required.");
            return;
        }

        flashcard.setQuestion(question);
        flashcard.setAnswer(answer);
        flashcard.setDeckName(deckName);

        try {
            flashcardRepository.updateFlashcard(flashcard);
            navigateToList(actionEvent);
        } catch (SQLException e) {
            statusLabel.setText("Error saving changes.");
        }
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) throws IOException {
        navigateToList(actionEvent);
    }

    private void navigateToList(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("flashcard-list-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
