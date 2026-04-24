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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SearchFlashcardController {

    @FXML private TextField searchField;
    @FXML private TableView<Flashcard> resultsTable;
    @FXML private TableColumn<Flashcard, String> questionColumn;
    @FXML private TableColumn<Flashcard, String> answerColumn;
    @FXML private TableColumn<Flashcard, String> deckColumn;
    @FXML private Label statusLabel;

    private final FlashcardDatabaseRepository repository = new FlashcardDatabaseRepository();

    @FXML
    public void initialize() {
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
            return;
        }

        try {
            List<Flashcard> results = repository.searchFlashcards(query);
            ObservableList<Flashcard> items = FXCollections.observableArrayList(results);
            resultsTable.setItems(items);
            statusLabel.setText(results.isEmpty() ? "No results found." : results.size() + " result(s) found.");
        } catch (SQLException e) {
            statusLabel.setText("Error searching flashcards.");
        }
    }

    @FXML
    public void handleClear(ActionEvent actionEvent) {
        searchField.clear();
        resultsTable.setItems(FXCollections.emptyObservableList());
        statusLabel.setText("");
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("flashcard-list-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
