# CS151 Flashcard Application (Milestone 0.4)

A JavaFX-based flashcard application that allows users to create, manage, and study decks of flashcards. This project is developed as part of CS151 coursework.

---

## 📌 Project Overview
The application provides an interactive interface for users to:
- Create flashcard decks
- Add, edit, and delete flashcards
- Navigate between different views
- Persist data locally

---

## 🎯 Milestone 0.4 Goals
- Implement core UI navigation (Home, Define Deck, Deck List)
- Establish MVC-style structure
- Integrate JavaFX with FXML views
- Implement basic data persistence (text file storage)
- Ensure application runs via Maven

---

## 🛠️ Technologies Used
- Java
- JavaFX
- Maven
- FXML (UI layout)
- CSS (styling)

---

## 📂 Project Structure
```
src/main/java/cs151/application
  ├── Main.java
  ├── HomeController.java
  ├── DeckListController.java
  ├── DefineDeckController.java
  ├── Deck.java
  ├── DeckFileRepository.java

src/main/resources/cs151/application
  ├── home-view.fxml
  ├── deck-list-view.fxml
  ├── define-deck-view.fxml
  ├── style.css

pom.xml
```

---

## 👥 Team Roles

| Name        | Role                | Responsibilities |
|------------|---------------------|------------------|
| Zakaria Bouaddou | Project Lead / Dev  | Core logic, integration, GitHub management, PM duties for submission |
| Roohan Poosala | UI Developer / Data Model | JavaFX views, styling (FXML, CSS), Data modeling |
| Esteban Madrigal | Backend Developer   | Data handling, file persistence.implementation |

---

## ▶️ How to Run

### 1. Clone the repository
```
git clone https://github.com/Zak-Dev21/cs151-flash-project.git
cd cs151-flash-project
```

### 2. Build project
```
mvn clean install
```

### 3. Run application
```
mvn javafx:run
```

---

## 📄 Features Implemented (0.4)
- Navigation between views
- Deck creation UI
- Deck listing UI
- Basic file storage (`decks.txt`)
- MVC separation (controllers, models, views)

---

## ⚠️ Known Limitations
- No advanced validation yet
- Limited UI polish
- File storage is basic (no database)

---

## 🚀 Future Improvements
- Add editing and deletion of decks
- Improve UI/UX
- Add search/filter functionality
- Replace file storage with database

---

## 👤 Author(s)
- Zakaria Bouaddou
- Rohal Poosala
- Esteban Madrigal

