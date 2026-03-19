package cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class DefineDeckController {

    @FXML
    private TextField deckNameField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ColorPicker deckColorPicker;

    @FXML
    private Label statusLabel;

    private final DeckFileRepository repository = new DeckFileRepository();

    @FXML
    public void handleSaveDeck() {
        String deckName = deckNameField.getText().trim();
        String description = descriptionArea.getText().trim();
        String color = deckColorPicker.getValue().toString();

        if (deckName.isEmpty()) {
            statusLabel.setText("Deck name is required.");
            return;
        }

        Deck deck = new Deck(deckName, description, color);

        try {
            repository.saveDeck(deck);
            statusLabel.setText("Deck saved successfully.");

            deckNameField.clear();
            descriptionArea.clear();
        } catch (IOException e) {
            statusLabel.setText("Error saving deck.");
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
