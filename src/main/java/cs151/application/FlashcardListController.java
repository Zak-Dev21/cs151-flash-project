package cs151.application;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for the Flashcard List page.
 * Displays all stored flashcards in a TableView,
 * sorted by most recent first.
 */
public class FlashcardListController {

    @FXML
    private TableView<Flashcard> flashcardTable;

    @FXML
    private TableColumn<Flashcard, String> questionColumn;

    @FXML
    private TableColumn<Flashcard, String> answerColumn;

    @FXML
    private TableColumn<Flashcard, String> deckColumn;

    @FXML
    private Label statusLabel;

    // Repository used to load flashcards from SQLite
    private final FlashcardDatabaseRepository repository = new FlashcardDatabaseRepository();

    /**
     * Runs automatically when the page loads.
     * Sets up table columns and loads stored flashcards.
     */
    @FXML
    public void initialize() {
        questionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getQuestion()));

        answerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAnswer()));

        deckColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDeckName()));

        loadFlashcards();
    }

    /**
     * Loads all flashcards from the database and shows them in the table.
     */
    private void loadFlashcards() {
        try {
            List<Flashcard> flashcardList = repository.getAllFlashcards();
            ObservableList<Flashcard> flashcards = FXCollections.observableArrayList(flashcardList);
            flashcardTable.setItems(flashcards);

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
