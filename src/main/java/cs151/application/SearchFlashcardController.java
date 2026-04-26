package cs151.application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

    @FXML private TextField searchField;
    @FXML private TableView<Flashcard> flashcardTable;
    @FXML private TableColumn<Flashcard, Number> idColumn;
    @FXML private TableColumn<Flashcard, String> questionColumn;
    @FXML private TableColumn<Flashcard, String> answerColumn;
    @FXML private TableColumn<Flashcard, String> deckColumn;
    @FXML private TableColumn<Flashcard, String> createdAtColumn;
    @FXML private Label statusLabel;

    private final FlashcardDatabaseRepository repository = new FlashcardDatabaseRepository();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        questionColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getQuestion()));
        answerColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAnswer()));
        deckColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDeckName()));
        createdAtColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCreatedAt()));

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

        loadAllFlashcards();
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
    public void handleSearch() {
        String query = searchField.getText().trim();
        try {
            List<Flashcard> results;
            if (query.isEmpty()) {
                results = repository.getAllFlashcards();
                statusLabel.setText("Showing all flashcards.");
            } else {
                results = repository.searchFlashcards(query);
                statusLabel.setText(results.isEmpty()
                        ? "No matching flashcards found."
                        : "Found " + results.size() + " matching flashcard(s).");
            }
            flashcardTable.setItems(FXCollections.observableArrayList(results));
        } catch (SQLException e) {
            statusLabel.setText("Error searching flashcards.");
        }
    }

    @FXML
    public void handleDeleteSelected() {
        Flashcard selected = flashcardTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a flashcard to delete.");
            return;
        }
        try {
            repository.deleteFlashcardById(selected.getId());
            statusLabel.setText("Flashcard deleted successfully.");
            handleSearch();
        } catch (SQLException e) {
            statusLabel.setText("Error deleting flashcard.");
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
            flashcardTable.setItems(FXCollections.observableArrayList(flashcards));
            statusLabel.setText(flashcards.isEmpty() ? "No flashcards found." : "Flashcards loaded successfully.");
        } catch (SQLException e) {
            statusLabel.setText("Error loading flashcards.");
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
