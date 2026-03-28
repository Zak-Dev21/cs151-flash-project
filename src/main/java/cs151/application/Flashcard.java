package cs151.application;

/**
 * Represents a flashcard belonging to a specific deck.
 * Each flashcard stores a question, answer, deck reference, and creation timestamp.
  */
public class Flashcard {
    private String question;
    private String answer;
    private String deckName;
    private String createdAt;

    // Default constructor (required for flexibility and frameworks if needed)
    public Flashcard() {
    }

    /**
     * Constructs a flashcard with all required fields.
     */

    public Flashcard(String question, String answer, String deckName, String createdAt) {
        this.question = question;
        this.answer = answer;
        this.deckName = deckName;
        this.createdAt = createdAt;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDeckName() {
        return deckName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
