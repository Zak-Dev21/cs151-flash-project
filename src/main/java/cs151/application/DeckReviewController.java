package cs151.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeckReviewController {

    private Deck currentDeck;
    private List<Flashcard> flashcards = new ArrayList<>();
    private int currentIndex = 0;

    private final FlashcardDatabaseRepository flashcardRepo = new FlashcardDatabaseRepository();

    public void setDeck(Deck deck) {
        this.currentDeck = deck;
        loadFlashcards();
    }

    private void loadFlashcards() {
        try {
            flashcards = flashcardRepo.getFlashcardsByDeck(currentDeck.getName());
            currentIndex = 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
