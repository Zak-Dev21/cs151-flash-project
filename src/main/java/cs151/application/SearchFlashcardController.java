package cs151.application;

import javafx.beans.property.SimpleIntegerProperty;
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

    private final FlashcardDatabaseRepository repository = new FlashcardDatabaseRepository();

    @FXML
    public void initialize() {
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
            return;
        }

        try {
            repository.deleteFlashcardById(selectedFlashcard.getId());
            statusLabel.setText("Flashcard deleted successfully.");

            // refresh table after delete
            handleSearch();

        } catch (SQLException e) {
            statusLabel.setText("Error deleting flashcard.");
            e.printStackTrace();
        }
    }

    @FXML
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
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
