package cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class EditDeckController {

    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField colorField;
    @FXML private Label statusLabel;

    private Deck deck;

    private final DeckDatabaseRepository repository = new DeckDatabaseRepository();

    public void setDeck(Deck deck) {
        this.deck = deck;
        nameField.setText(deck.getName());
        descriptionArea.setText(deck.getDescription());
        colorField.setText(deck.getColor());
    }

    @FXML
    public void handleSave(ActionEvent actionEvent) throws IOException {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        String color = colorField.getText().trim();

        if (name.isEmpty()) {
            statusLabel.setText("Deck name is required.");
            return;
        }

        deck.setName(name);
        deck.setDescription(description);
        deck.setColor(color);

        try {
            repository.updateDeck(deck);
            navigateToDeckList(actionEvent);
        } catch (SQLException e) {
            statusLabel.setText("Error saving deck.");
        }
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) throws IOException {
        navigateToDeckList(actionEvent);
    }

    private void navigateToDeckList(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("deck-list-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
