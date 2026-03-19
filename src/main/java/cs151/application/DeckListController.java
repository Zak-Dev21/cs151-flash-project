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
import java.util.List;

public class DeckListController {

    @FXML
    private TableView<Deck> deckTable;

    @FXML
    private TableColumn<Deck, String> nameColumn;

    @FXML
    private TableColumn<Deck, String> descriptionColumn;

    @FXML
    private TableColumn<Deck, String> colorColumn;

    @FXML
    private Label statusLabel;

    private final DeckFileRepository repository = new DeckFileRepository();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        colorColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getColor()));

        loadDecks();
    }

    private void loadDecks() {
        try {
            List<Deck> deckList = repository.getAllDecks();
            ObservableList<Deck> decks = FXCollections.observableArrayList(deckList);
            deckTable.setItems(decks);

            if (decks.isEmpty()) {
                statusLabel.setText("No decks found.");
            } else {
                statusLabel.setText("Decks loaded successfully.");
            }
        } catch (IOException e) {
            statusLabel.setText("Error loading decks.");
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
