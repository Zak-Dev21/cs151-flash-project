package cs151.application;

public class Deck {
    private String name;
    private String description;
    private String color;
    private String createdAt;

    public Deck() {
    }

    public Deck(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.createdAt = "";
    }

    public Deck(String name, String description, String color, String createdAt) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getCreatedAt() { return createdAt;}

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
