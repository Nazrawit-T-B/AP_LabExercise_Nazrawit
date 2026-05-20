package com.app.client;
import com.app.common.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import java.io.*;
import java.nio.file.*;

public class ChatClientApp extends Application {

    private ChatClient client;
    private String username;
    private VBox messageBox;
    private ScrollPane scrollPane;
    private TextField inputField;

    @Override
    public void start(Stage stage) {
        username = promptUsername();
        if (username == null || username.isBlank()) Platform.exit();

        client = new ChatClient(msg -> Platform.runLater(() -> displayMessage(msg)));

        try {
            client.connect("localhost", 5000, username);
        } catch (IOException e) {
            showAlert("Cannot connect to server: " + e.getMessage());
            Platform.exit();
            return;
        }
        messageBox = new VBox(8);
        messageBox.setPadding(new Insets(12));
        messageBox.setFillWidth(true);

        scrollPane = new ScrollPane(messageBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVvalue(1.0);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);


        inputField = new TextField();
        inputField.setPromptText("Type a message...");
        inputField.setOnAction(e -> sendText());
        HBox.setHgrow(inputField, Priority.ALWAYS);

        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(e -> sendText());

        Button imageBtn = new Button(" Image");
        imageBtn.setOnAction(e -> sendImage());

        HBox inputBar = new HBox(8, inputField, sendBtn, imageBtn);
        inputBar.setPadding(new Insets(8));
        inputBar.setAlignment(Pos.CENTER);

        VBox root = new VBox(scrollPane, inputBar);
        root.setPrefSize(600, 500);
        Scene scene=new Scene(root);
        stage.setTitle("Chat — " + username);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            try { client.disconnect(); } catch (IOException ignored) {}
        });
        stage.show();
    }



    private void sendText() {
        String text = inputField.getText().trim();
        if (text.isBlank()) return;
        try {
            Message msg = new Message(Message.Type.TEXT, username, text);
            client.send(msg);
            displayMessage(msg);
            inputField.clear();
        } catch (IOException e) {
            showAlert("Failed to send: " + e.getMessage());
        }
    }

    private void sendImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Image");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = chooser.showOpenDialog(null);
        if (file == null) return;

        try {
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            Message msg = new Message(username, imageBytes);
            client.send(msg);
            displayMessage(msg); // Show own image locally
        } catch (IOException e) {
            showAlert("Failed to send image: " + e.getMessage());
        }
    }

    private void displayMessage(Message msg) {
        VBox bubble = new VBox(4);
        boolean isMine = msg.getSender().equals(username);

        Label senderLabel = new Label(msg.getSender());
        senderLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 11px; -fx-text-fill: #666;");

        if (msg.getType() == Message.Type.TEXT) {
            Label textLabel = new Label(msg.getText());
            textLabel.setWrapText(true);
            textLabel.setMaxWidth(400);
            textLabel.setStyle(isMine
                    ? "-fx-background-color:purple; -fx-text-fill: white; -fx-padding: 8 12; -fx-background-radius: 16;"
                    : "-fx-background-color: #e5e5ea; -fx-text-fill: black; -fx-padding: 8 12; -fx-background-radius: 16;");
            bubble.getChildren().addAll(senderLabel, textLabel);

        } else if (msg.getType() == Message.Type.IMAGE) {
            Image image = new Image(new ByteArrayInputStream(msg.getImageData()));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(250);
            imageView.setPreserveRatio(true);
            bubble.getChildren().addAll(senderLabel, imageView);

        } else { // JOIN / LEAVE
            Label info = new Label(msg.getText());
            info.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
            messageBox.getChildren().add(info);
            autoScroll();
            return;
        }

        bubble.setAlignment(isMine ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messageBox.getChildren().add(bubble);
        autoScroll();
    }

    private void autoScroll() {
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    private String promptUsername() {
        TextInputDialog dialog = new TextInputDialog("User" + (int)(Math.random() * 1000));
        dialog.setTitle("Join Chat");
        dialog.setHeaderText("Enter your username:");
        return dialog.showAndWait().orElse(null);
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}
