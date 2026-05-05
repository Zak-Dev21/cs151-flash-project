package cs151.application;

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
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewDeckListController {

    @FXML private TextField searchField;
    @FXML private TableView<Deck> deckTable;
    @FXML private TableColumn<Deck, String> nameColumn;
    @FXML private TableColumn<Deck, String> descriptionColumn;
    @FXML private TableColumn<Deck, String> createdAtColumn;
    @FXML private Label statusLabel;
    @FXML private ObservableList<Deck> allDecks;

    private final DeckDatabaseRepository deckRepository = new DeckDatabaseRepository();

    @FXML
    public void initialize() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> handleSearch());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            handleSearch();
        });
        nameColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName()));

        descriptionColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescription()));

        createdAtColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCreatedAt()));

        deckTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Deck selectedDeck = deckTable.getSelectionModel().getSelectedItem();
                if (selectedDeck != null) {
                    try {
                        openDeckReview(selectedDeck);
                    } catch (IOException e) {
                        statusLabel.setText("Error opening deck review page.");
                    }
                }
            }
        });

        loadDecks();
        searchField.textProperty().addListener(
                (javafx.beans.value.ObservableValue<? extends String> obs,
                 String oldValue,
                 String newValue) -> {
                    handleSearch();
                }
        );
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();

        try {
            List<Deck> decks = deckRepository.getAllDecks();

            List<Deck> filteredDecks = decks.stream()
                    .filter(deck ->
                            deck.getName().toLowerCase().contains(query)
                                    || deck.getDescription().toLowerCase().contains(query))
                    .collect(Collectors.toList());

            deckTable.setItems(FXCollections.observableArrayList(filteredDecks));

            statusLabel.setText(filteredDecks.size() + " deck(s) found.");
        } catch (SQLException e) {
            statusLabel.setText("Error searching decks.");
        }
    }

    private void loadDecks() {
        try {
            List<Deck> decks = deckRepository.getAllDecks();
            deckTable.setItems(FXCollections.observableArrayList(decks));

            if (decks.isEmpty()) {
                statusLabel.setText("No decks found.");
            } else {
                statusLabel.setText(decks.size() + " deck(s) loaded.");
            }
        } catch (SQLException e) {
            statusLabel.setText("Error loading decks.");
        }
    }

    private void openDeckReview(Deck deck) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("deck-review-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());

        DeckReviewController controller = loader.getController();
        controller.setDeck(deck.getName());

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