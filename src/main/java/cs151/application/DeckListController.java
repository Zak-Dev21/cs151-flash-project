package cs151.application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DeckListController {

    @FXML private TableView<Deck> deckTable;
    @FXML private TableColumn<Deck, String> nameColumn;
    @FXML private TableColumn<Deck, String> descriptionColumn;
    @FXML private TableColumn<Deck, String> colorColumn;
    @FXML private Label statusLabel;

    private final DeckDatabaseRepository repository = new DeckDatabaseRepository();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        descriptionColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));
        colorColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getColor()));

        deckTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Deck selected = deckTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        openEditView(selected);
                    } catch (IOException ex) {
                        statusLabel.setText("Error opening edit view.");
                    }
                }
            }
        });

        loadDecks();
    }

    private void loadDecks() {
        try {
            List<Deck> deckList = repository.getAllDecks();
            ObservableList<Deck> decks = FXCollections.observableArrayList(deckList);
            deckTable.setItems(decks);
            statusLabel.setText(decks.isEmpty() ? "No decks found." : "Decks loaded successfully.");
        } catch (SQLException e) {
            statusLabel.setText("Error loading decks.");
        }
    }

    @FXML
    public void handleDeleteSelected() {
        Deck selected = deckTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a deck to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Deck");
        confirm.setHeaderText("Delete \"" + selected.getName() + "\"?");
        confirm.setContentText("This will also permanently delete all flashcards linked to this deck.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                repository.deleteDeck(selected.getId());
                statusLabel.setText("Deck and linked flashcards deleted.");
                loadDecks();
            } catch (SQLException e) {
                statusLabel.setText("Error deleting deck.");
            }
        }
    }

    private void openEditView(Deck deck) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("edit-deck-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        EditDeckController controller = loader.getController();
        controller.setDeck(deck);
        Stage stage = (Stage) deckTable.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
