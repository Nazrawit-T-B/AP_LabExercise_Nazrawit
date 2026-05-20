# AP_LabExercise

## This Repository contains the following:- 
- A Chat App
- A Poker Game
- A Note Pad Application

#### Tech Stack used: - 
- Java
- Css

## How to run each projects 

### Requirements 
- Java 17 or higher
- JavaFX SDK 17 or higher
- An IDE (IntelliJ IDEA recommended)

### General Instruction 
1. Clone or download the project
2. Open the project in IntelliJ IDEA
3. Add the JavaFX SDK to your project libraries:
   - Go to **File → Project Structure → Libraries → + → Java**
   - Select your JavaFX SDK `lib` folder
4. Configure the run configuration:
   - Go to **Run → Edit Configurations**
   - Under **VM options** add:
     ```
     --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
     ```
5. Run `NotePad.java` inside `com/game/`


