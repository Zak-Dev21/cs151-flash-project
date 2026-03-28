# Name of application: Flashcards

# Who did What

# Version: 0.5
1. Zakaria Bouaddou: Migrated deck storage to SQLite, updated controllers to use database, and implemented flashcard backend (model + repository)
2. Esteban Madrigal: To implement flashcard controllers and UI integration
3. Rohan Poosala: Minor UI updates and validation improvements


# Version: 0.4
1. Rohan Poosala: Implemented Define Deck save functionality and file storage (Deck + Repository)
2. Zakaria Bouaddou: Implemented Home page navigation and controller logic
3. Esteban Madrigal: Implemented Deck List page with TableView and sorting (A–Z, case-insensitive)

# Version: 0.2
1. Rohan Poosala: UI design and layout
2. Zakaria Bouaddou: Homepage implementation
3. Esteban Madrigal: Deck page implementation

# Technical-Spec
1. Rohan Poosala: Designed data model (Deck class and file storage structure)
2. Zakaria Bouaddou: Managed controller logic and application flow
3. Esteban Madrigal: Created UML diagrams and integration design

# Functional-Spec
1. Rohan Poosala: Defined deck creation behavior and validation
2. Zakaria Bouaddou: Designed navigation between pages
3. Esteban Madrigal: Created UI mockups and layout design

# Any other instruction that users need to know:
- Run the project using Maven: mvn clean javafx:run
- Use "Define Deck" to create and save a deck
- Use "View Decks" to see all saved decks in a table
- Decks are stored locally in an SQLite database (flashcards.db)
- Decks are displayed by most recent creation time first
- All pages include a Back button for navigation
