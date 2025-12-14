# TicTacToe (Visual Programming Project - Group 4)

A Java-based Tic Tac Toe game featuring a graphical user interface (GUI), user authentication, and a scoreboard system. This project was developed as part of a Visual Programming course.

## 📋 Features

* **User Authentication**: Secure login system for players (`LoginScreen.java`, `UserSession.java`).
* **Interactive GUI**: Built with Java Swing/AWT for a responsive desktop experience.
* **Game Modes**: Select different ways to play via the `GameModeScreen`.
* **Score Tracking**:View high scores and player history (`ScoreboardScreen.java`).
* **Database Integration**: persistent storage for user credentials and game scores.

## 🛠 Technologies Used

* **Language**: Java
* **GUI Framework**: Java Swing (assumed based on standard Visual Programming curriculum)
* **Database**: Postgresql Supabase (Requires JDBC driver)
* **IDE**: Compatible with NetBeans, IntelliJ IDEA, or Eclipse.

## 📂 Project Structure

* `Main.java`: The entry point of the application.
* `DbCon.java`: Handles the connection to the database.
* `GameBoard.java`: Contains the core logic and UI for the Tic Tac Toe board.
* `LoginScreen.java` & `WelcomeScreen.java`: Handles user entry and navigation.
* `UserSession.java`: Manages the currently logged-in user's state.
* `ScoreboardScreen.java`: Displays game history/rankings.

## 🚀 Installation & Setup

### Prerequisites
* [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/) (Version 8 or higher recommended).
* A MySQL Database server (e.g., XAMPP, WAMP, or standalone MySQL).

### Step 1: Clone the Repository
```bash
git clone [https://github.com/apiz23/TicTacToe.git](https://github.com/apiz23/TicTacToe.git)
