package cs151.application;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DeckFileRepository
{
    private static final String FILE_NAME = "decks.txt";
    private final Path filePath;

    public DeckFileRepository() {
        this.filePath = Paths.get(FILE_NAME);
    }

    public void saveDeck(Deck deck) throws IOException {
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardOpenOption.APPEND)) {

            String safeName = clean(deck.getName());
            String safeDescription = clean(deck.getDescription());
            String safeColor = clean(deck.getColor());

            writer.write(safeName + "|" + safeDescription + "|" + safeColor);
            writer.newLine();
        }
    }

    public List<Deck> getAllDecks() throws IOException {
        List<Deck> decks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return decks;
        }

        List<String> lines = Files.readAllLines(filePath);

        for (String line : lines) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 3) {
                Deck deck = new Deck(parts[0], parts[1], parts[2]);
                decks.add(deck);
            }
        }

        decks.sort(Comparator.comparing(deck -> deck.getName().toLowerCase()));
        return decks;
    }

    private String clean(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("|", "/").replace("\n", " ").replace("\r", " ");
    }
}
