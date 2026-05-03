package cs151.application;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeckReviewController {

    private static final String FILTER_ALL = "All";
    private static final List<String> STATUS_OPTIONS = List.of("New", "Learning", "Mastered");

    // Set by the upstream deck-list controller before the scene is shown.
    @FXML private Label deckNameLabel;

    // Filter + status
    @FXML private ComboBox<String> filterComboBox;
    @FXML private ComboBox<String> statusComboBox;

    // Card content
    @FXML private TextArea questionArea;
    @FXML private TextArea answerArea;

    // Metadata
    @FXML private Label createdAtLabel;
    @FXML private Label lastReviewedLabel;

    // Navigation
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label progressLabel;

    // Feedback
    @FXML private Label statusLabel;

    private final FlashcardDatabaseRepository flashcardRepo = new FlashcardDatabaseRepository();

    private String deckName;
    private List<Flashcard> allCards = new ArrayList<>();
    private List<Flashcard> filteredCards = new ArrayList<>();
    private int currentIndex = 0;

    @FXML
    public void initialize() {
        filterComboBox.setItems(
                FXCollections.observableArrayList(FILTER_ALL, "New", "Learning", "Mastered"));
        filterComboBox.setValue(FILTER_ALL);

        statusComboBox.setItems(FXCollections.observableArrayList(STATUS_OPTIONS));
    }

    /**
     * Called by the review-deck-list controller to inject the selected deck
     * before this scene is displayed.
     */
    public void setDeck(String deckName) {
        this.deckName = deckName;
        deckNameLabel.setText(deckName);
        loadAllCards();
        applyFilter();
    }

    // ── Loading ──────────────────────────────────────────────────────────────

    private void loadAllCards() {
        try {
            allCards = flashcardRepo.getFlashcardsByDeck(deckName);
        } catch (SQLException e) {
            statusLabel.setText("Error loading flashcards.");
            allCards = new ArrayList<>();
        }
    }

    // ── Filter ───────────────────────────────────────────────────────────────

    @FXML
    public void handleFilterChanged() {
        currentIndex = 0;
        applyFilter();
    }

    private void applyFilter() {
        String filter = filterComboBox.getValue();
        if (filter == null || filter.equals(FILTER_ALL)) {
            filteredCards = new ArrayList<>(allCards);
        } else {
            filteredCards = allCards.stream()
                    .filter(f -> filter.equals(f.getStatus()))
                    .collect(Collectors.toList());
        }
        showCard();
    }

    // ── Display ──────────────────────────────────────────────────────────────

    private void showCard() {
        if (filteredCards.isEmpty()) {
            clearCard("No cards match the selected filter.");
            return;
        }

        Flashcard card = filteredCards.get(currentIndex);
        questionArea.setText(card.getQuestion());
        answerArea.setText(card.getAnswer());
        statusComboBox.setValue(card.getStatus() != null ? card.getStatus() : "New");
        createdAtLabel.setText(card.getCreatedAt() != null ? card.getCreatedAt() : "—");
        lastReviewedLabel.setText(card.getLastReviewedAt() != null ? card.getLastReviewedAt() : "Never");
        progressLabel.setText((currentIndex + 1) + " of " + filteredCards.size());
        prevButton.setDisable(currentIndex == 0);
        nextButton.setDisable(currentIndex == filteredCards.size() - 1);
        statusLabel.setText("");
    }

    private void clearCard(String message) {
        questionArea.clear();
        answerArea.clear();
        statusComboBox.setValue(null);
        createdAtLabel.setText("—");
        lastReviewedLabel.setText("—");
        progressLabel.setText("0 of 0");
        prevButton.setDisable(true);
        nextButton.setDisable(true);
        statusLabel.setText(message);
    }

    // ── Navigation ───────────────────────────────────────────────────────────

    @FXML
    public void handlePrevious() {
        if (currentIndex > 0) {
            currentIndex--;
            showCard();
        }
    }

    @FXML
    public void handleNext() {
        if (currentIndex < filteredCards.size() - 1) {
            currentIndex++;
            showCard();
        }
    }

    // ── Save ─────────────────────────────────────────────────────────────────

    @FXML
    public void handleSave() {
        if (filteredCards.isEmpty()) return;

        Flashcard card = filteredCards.get(currentIndex);
        card.setQuestion(questionArea.getText().trim());
        card.setAnswer(answerArea.getText().trim());
        card.setStatus(statusComboBox.getValue());
        card.setLastReviewedAt(LocalDateTime.now().toString());

        try {
            flashcardRepo.updateFlashcard(card);
            flashcardRepo.updateReviewStatus(card);
            lastReviewedLabel.setText(card.getLastReviewedAt());
            statusLabel.setText("Saved.");
        } catch (SQLException e) {
            statusLabel.setText("Error saving card.");
        }
    }

    // ── Back ─────────────────────────────────────────────────────────────────

    @FXML
    public void handleBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("review-deck-list-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
