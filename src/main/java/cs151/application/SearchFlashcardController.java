package cs151.application;

<<<<<<< HEAD
=======
import javafx.beans.property.SimpleIntegerProperty;
>>>>>>> origin/main
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SearchFlashcardController {

<<<<<<< HEAD
    @FXML private TextField searchField;
    @FXML private TableView<Flashcard> resultsTable;
    @FXML private TableColumn<Flashcard, String> questionColumn;
    @FXML private TableColumn<Flashcard, String> answerColumn;
    @FXML private TableColumn<Flashcard, String> deckColumn;
    @FXML private Label statusLabel;
=======
    @FXML
    private TextField searchField;

    @FXML
    private TableView<Flashcard> flashcardTable;

    @FXML
    private TableColumn<Flashcard, Number> idColumn;

    @FXML
    private TableColumn<Flashcard, String> questionColumn;

    @FXML
    private TableColumn<Flashcard, String> answerColumn;

    @FXML
    private TableColumn<Flashcard, String> deckColumn;

    @FXML
    private TableColumn<Flashcard, String> createdAtColumn;

    @FXML
    private Label statusLabel;
>>>>>>> origin/main

    private final FlashcardDatabaseRepository repository = new FlashcardDatabaseRepository();

    @FXML
    public void initialize() {
<<<<<<< HEAD
        questionColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getQuestion()));
        answerColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAnswer()));
        deckColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDeckName()));

        // Search on Enter key
        searchField.setOnAction(e -> handleSearch(null));
    }

    @FXML
    public void handleSearch(ActionEvent actionEvent) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            statusLabel.setText("Enter a search term.");
            resultsTable.setItems(FXCollections.emptyObservableList());
=======
        idColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()));

        questionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getQuestion()));

        answerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAnswer()));

        deckColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDeckName()));

        createdAtColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreatedAt()));

        loadAllFlashcards();
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().trim();

        try {
            List<Flashcard> results;

            if (query.isEmpty()) {
                results = repository.getAllFlashcards();
                statusLabel.setText("Showing all flashcards.");
            } else {
                results = repository.searchFlashcards(query);
                statusLabel.setText("Found " + results.size() + " matching flashcard(s).");
            }

            ObservableList<Flashcard> flashcards = FXCollections.observableArrayList(results);
            flashcardTable.setItems(flashcards);

            if (results.isEmpty()) {
                statusLabel.setText("No matching flashcards found.");
            }

        } catch (SQLException e) {
            statusLabel.setText("Error searching flashcards.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteSelected() {
        Flashcard selectedFlashcard = flashcardTable.getSelectionModel().getSelectedItem();

        if (selectedFlashcard == null) {
            statusLabel.setText("Please select a flashcard to delete.");
>>>>>>> origin/main
            return;
        }

        try {
<<<<<<< HEAD
            List<Flashcard> results = repository.searchFlashcards(query);
            ObservableList<Flashcard> items = FXCollections.observableArrayList(results);
            resultsTable.setItems(items);
            statusLabel.setText(results.isEmpty() ? "No results found." : results.size() + " result(s) found.");
        } catch (SQLException e) {
            statusLabel.setText("Error searching flashcards.");
=======
            repository.deleteFlashcardById(selectedFlashcard.getId());
            statusLabel.setText("Flashcard deleted successfully.");

            // refresh table after delete
            handleSearch();

        } catch (SQLException e) {
            statusLabel.setText("Error deleting flashcard.");
            e.printStackTrace();
>>>>>>> origin/main
        }
    }

    @FXML
<<<<<<< HEAD
    public void handleClear(ActionEvent actionEvent) {
        searchField.clear();
        resultsTable.setItems(FXCollections.emptyObservableList());
        statusLabel.setText("");
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("flashcard-list-view.fxml"));
=======
    public void handleShowAll() {
        searchField.clear();
        loadAllFlashcards();
    }

    private void loadAllFlashcards() {
        try {
            List<Flashcard> flashcards = repository.getAllFlashcards();
            ObservableList<Flashcard> observableList = FXCollections.observableArrayList(flashcards);
            flashcardTable.setItems(observableList);

            if (flashcards.isEmpty()) {
                statusLabel.setText("No flashcards found.");
            } else {
                statusLabel.setText("Flashcards loaded successfully.");
            }
        } catch (SQLException e) {
            statusLabel.setText("Error loading flashcards.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBackHome(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
>>>>>>> origin/main
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
