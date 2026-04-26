package cs151.application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchFlashcardController {

    private static final String ALL_DECKS = "All Decks";

    @FXML private ComboBox<String> deckFilter;
    @FXML private TextField searchField;
    @FXML private TableView<Flashcard> flashcardTable;
    @FXML private TableColumn<Flashcard, Number> idColumn;
    @FXML private TableColumn<Flashcard, String> questionColumn;
    @FXML private TableColumn<Flashcard, String> answerColumn;
    @FXML private TableColumn<Flashcard, String> deckColumn;
    @FXML private TableColumn<Flashcard, String> createdAtColumn;
    @FXML private Label statusLabel;

    private final FlashcardDatabaseRepository flashcardRepo = new FlashcardDatabaseRepository();
    private final DeckDatabaseRepository deckRepo = new DeckDatabaseRepository();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        questionColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getQuestion()));
        answerColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAnswer()));
        deckColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDeckName()));
        createdAtColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCreatedAt()));

        loadDeckFilter();

        flashcardTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Flashcard selected = flashcardTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        openEditView(selected);
                    } catch (IOException ex) {
                        statusLabel.setText("Error opening edit view.");
                    }
                }
            }
        });

        loadFiltered();
    }

    private void loadDeckFilter() {
        deckFilter.getItems().clear();
        deckFilter.getItems().add(ALL_DECKS);
        try {
            List<Deck> decks = deckRepo.getAllDecks();
            for (Deck d : decks) {
                deckFilter.getItems().add(d.getName());
            }
        } catch (SQLException e) {
            statusLabel.setText("Error loading decks.");
        }
        deckFilter.setValue(ALL_DECKS);
    }

    @FXML
    public void handleDeckSelected() {
        searchField.clear();
        loadFiltered();
    }

    @FXML
    public void handleSearch() {
        loadFiltered();
    }

    @FXML
    public void handleShowAll() {
        deckFilter.setValue(ALL_DECKS);
        searchField.clear();
        loadFiltered();
    }

    private void loadFiltered() {
        String selectedDeck = deckFilter.getValue();
        String query = searchField.getText().trim().toLowerCase();

        try {
            List<Flashcard> base;
            if (selectedDeck == null || selectedDeck.equals(ALL_DECKS)) {
                base = flashcardRepo.getAllFlashcards();
            } else {
                base = flashcardRepo.getFlashcardsByDeck(selectedDeck);
            }

            List<Flashcard> results = query.isEmpty() ? base : base.stream()
                    .filter(f -> f.getQuestion().toLowerCase().contains(query)
                              || f.getAnswer().toLowerCase().contains(query))
                    .collect(Collectors.toList());

            flashcardTable.setItems(FXCollections.observableArrayList(results));

            if (results.isEmpty()) {
                statusLabel.setText("No flashcards found.");
            } else {
                statusLabel.setText(results.size() + " flashcard(s) shown.");
            }
        } catch (SQLException e) {
            statusLabel.setText("Error loading flashcards.");
        }
    }

    private void openEditView(Flashcard flashcard) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("edit-flashcard-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        EditFlashcardController controller = loader.getController();
        controller.setFlashcard(flashcard);
        Stage stage = (Stage) flashcardTable.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleDeleteSelected() {
        Flashcard selected = flashcardTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a flashcard to delete.");
            return;
        }
        try {
            flashcardRepo.deleteFlashcardById(selected.getId());
            statusLabel.setText("Flashcard deleted successfully.");
            loadFiltered();
        } catch (SQLException e) {
            statusLabel.setText("Error deleting flashcard.");
        }
    }

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
