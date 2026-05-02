package cs151.application;

/**
 * Represents a flashcard belonging to a specific deck.
 * Each flashcard stores a question, answer, deck reference, and creation timestamp.
  */
public class Flashcard {
    private int id;
    private String question;
    private String answer;
    private String deckName;
    private String createdAt;
    private String status;
    private String lastReviewedAt;

    // Default constructor (required for flexibility and frameworks if needed)
    public Flashcard() {
    }

    /**
     * Constructs a flashcard with all required fields.
     */

    public Flashcard(int id, String question, String answer, String deckName, String createdAt, String status, String lastReviewedAt) {
        this.question = question;
        this.answer = answer;
        this.deckName = deckName;
        this.createdAt = createdAt;
        this.id = id;
        this.status = status;
        this.lastReviewedAt = lastReviewedAt;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public String getStatus() {
        return status;
    }

    public String getLastReviewedAt() {
        return lastReviewedAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLastReviewedAt(String lastReviewedAt) {
        this.lastReviewedAt = lastReviewedAt;
    }
}
