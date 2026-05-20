package com.game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


public class NotePad extends Application {

    private NoteP note=new NoteP();
    BorderPane root=new BorderPane();
    TabPane tab=new TabPane();
    int Tabcounter=0;

    private TextArea getCurrentTextArea() {
        Tab selected = tab.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getContent() instanceof TextArea) {
            return (TextArea) selected.getContent();
        }
        selected.getStyleClass().add("tabs");
        return null;
    }

    private void saveCurrentTab(Tab tab) {
        TextArea area = (TextArea) tab.getContent();
        File file = (File) tab.getUserData();

        if (file == null) {
            FileChooser chooser = new FileChooser();
            file = chooser.showSaveDialog(root.getScene().getWindow());

            if (file == null) return;

            tab.setUserData(file);
            tab.setText(file.getName());
        }

        note.writeFile(file.getAbsolutePath(), area.getText());


        tab.getProperties().put("saved", true);
        if (tab.getText().startsWith("*")) {
            tab.setText(tab.getText().substring(1));
        }
    }
    private void addCloseHandler(Tab tab) {
        tab.setOnCloseRequest(event -> {
            Boolean saved = (Boolean) tab.getProperties().get("saved");

            if (saved == null || !saved) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                DialogPane pane=alert.getDialogPane();
                pane.getStyleClass().add("calert");
                alert.setTitle("Unsaved Changes");
                alert.setHeaderText("You have unsaved changes.");
                alert.setContentText("Do you want to save before closing?");

                ButtonType save = new ButtonType("Save");
                ButtonType dontSave = new ButtonType("Don't Save");
                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(save, dontSave, cancel);

                ButtonType result = alert.showAndWait().orElse(cancel);

                if (result == save) {
                    saveCurrentTab(tab);
                }
                else if (result == cancel) {
                    event.consume();
                }

            }
        });
    }

    public MenuBar Appmenu(BorderPane root){

        MenuBar menu=new MenuBar();
        menu.getStyleClass().add("menubar");
        tab.getStyleClass().add("tabs");

        Menu filemenu=new Menu("File");
        Menu editmenu=new Menu("Edit");

        Menu permissionmenu=new Menu("Permissions");

        MenuItem newitem=new MenuItem("New");
        MenuItem openitem=new MenuItem("Open");
        MenuItem saveitem=new MenuItem("Save");
        MenuItem saveasitem=new MenuItem("Save as");
        MenuItem exititem=new MenuItem("Exit");

        //cut,copy,paste,delete

        MenuItem cutitem=new MenuItem("Cut");
        MenuItem copyitem=new MenuItem("Copy");
        MenuItem pasteitem=new MenuItem("Paste");
        MenuItem renameitem=new MenuItem("Rename");
        MenuItem deleteitem=new MenuItem("Delete");


        //set as Read Only
        MenuItem readonlyitem=new MenuItem(" Set as Read Only");

        filemenu.getItems().addAll(newitem,openitem,saveitem,saveasitem,exititem);
        editmenu.getItems().addAll(cutitem,copyitem,pasteitem,renameitem,deleteitem);

        permissionmenu.getItems().addAll(readonlyitem);
        menu.getMenus().addAll(filemenu,editmenu,permissionmenu);

        newitem.setOnAction(e -> {

            StackPane overlay = new StackPane();
            Label label = new Label("Enter file name:");
            TextField fileNameField = new TextField();
            Button createButton = new Button("Create");
            createButton.getStyleClass().add("btn-create");
            Button closeBtn = new Button("✕");
            HBox topBar=new HBox(closeBtn);
            topBar.setAlignment(Pos.TOP_RIGHT);
            VBox content=new VBox(10,label,fileNameField,createButton);
            closeBtn.getStyleClass().add("btn-cancel-transfer");
            VBox form = new VBox(5,topBar,content);
            form.setAlignment(Pos.TOP_CENTER);

            form.setMaxSize(300, 150);
            form.setPrefSize(300, 150);

            form.setStyle("-fx-background-color: white; " +
                    "-fx-padding: 20; " +
                    "-fx-background-radius: 5; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");

            overlay.getChildren().add(form);

            root.setCenter(overlay);

            createButton.setOnAction(ev -> {
                String name = fileNameField.getText().trim();
                if (name.isEmpty()) {
                    name = "Untitled " + (++Tabcounter);
                }

                TextArea newTextArea = new TextArea();
                Tab newTab = new Tab(name,newTextArea);
                addCloseHandler(newTab);
                newTab.getStyleClass().add("tabs");
                newTab.setUserData(null);
                newTab.getProperties().put("saved",true);
                newTextArea.textProperty().addListener((obs,old,newT)->{
                    Boolean saved = (Boolean) tab.getProperties().get("saved");

                    if (saved == null || saved) {
                        newTab.getProperties().put("saved", false);

                        if (!newTab.getText().startsWith("*")) {
                            newTab.setText("*" + newTab.getText());
                        }
                    }
                });
                tab.getTabs().add(newTab);
                tab.getSelectionModel().select(newTab);
                root.setCenter(tab);
            });
            closeBtn.setOnAction(eve->{

                root.setCenter(tab);

            });
        });
        newitem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));

        openitem.setOnAction(e->{
            FileChooser choose=new FileChooser();
            choose.setTitle("Open File");

            File file=choose.showOpenDialog(root.getScene().getWindow());
            if(file!=null){


                String content=note.readFile(file.getAbsolutePath());
                TextArea newTextArea = new TextArea(content);
                String name=file.getName();

                Tab newTab=new Tab(name,newTextArea);
                addCloseHandler(newTab);
                newTab.getStyleClass().add("tabs");
                newTab.setUserData(file);
                newTab.getProperties().put("saved",true);
                newTextArea.textProperty().addListener((obs, oldText, newText) -> {
                    Boolean saved = (Boolean) newTab.getProperties().get("saved");

                    if (saved == null || saved) {
                        newTab.getProperties().put("saved", false);

                        if (!newTab.getText().startsWith("*")) {
                            newTab.setText("*" + newTab.getText());
                        }
                    }
                });

                tab.getTabs().add(newTab);
                tab.getSelectionModel().select(newTab);



                root.setCenter(tab);


            }
        });

        saveitem.setOnAction(e->{
            Tab selectedTab = tab.getSelectionModel().getSelectedItem();

            TextArea currentArea = getCurrentTextArea();
            if (selectedTab == null || currentArea == null) return;
            selectedTab.getStyleClass().add("tabs");
            File file=(File)selectedTab.getUserData();
            if(file!=null){
                saveCurrentTab(selectedTab);
            }else{
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Save File");
                chooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files","*.txt"),
                        new FileChooser.ExtensionFilter("Python","*.py"),
                        new FileChooser.ExtensionFilter("C","*.c"),
                        new FileChooser.ExtensionFilter("All Files","*.*")
                );

                File filenew = chooser.showSaveDialog(root.getScene().getWindow());

                if (filenew != null) {
                  saveCurrentTab(selectedTab);

                }
            }
        });

        saveitem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

        saveasitem.setOnAction(e->{
            Tab selectedTab = tab.getSelectionModel().getSelectedItem();
            selectedTab.getStyleClass().add("tabs");
            TextArea currentArea = getCurrentTextArea();
            if (selectedTab == null || currentArea == null) return;
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save File As");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files","*.txt"),
                    new FileChooser.ExtensionFilter("Python","*.py"),
                    new FileChooser.ExtensionFilter("C","*.c"),
                    new FileChooser.ExtensionFilter("All Files","*.*"));

            File file = chooser.showSaveDialog(root.getScene().getWindow());

            if (file != null) {
                saveCurrentTab(selectedTab);
            }
        });

        exititem.setOnAction(e->{
            Platform.exit();
        });

        cutitem.setOnAction(e -> {
           TextArea a=getCurrentTextArea();
           if(a!=null)
               a.cut();
        });

        copyitem.setOnAction(e->{
            TextArea a=getCurrentTextArea();
            if(a!=null)
                a.copy();
        });

        pasteitem.setOnAction(e->{
           TextArea a=getCurrentTextArea();
           if(a!=null)
                a.paste();
        });

        deleteitem.setOnAction(e->{

            TextArea a=getCurrentTextArea();
            if(a!=null)
                a.replaceSelection("");

        });
        renameitem.setOnAction(e->{
            StackPane overlay = new StackPane();
            Label label = new Label("Enter file name:");
            TextField fileNameField = new TextField();
            Button renameButton = new Button("Create");
            renameButton.getStyleClass().add("btn-create");
            Button closeBtn = new Button("✕");
            HBox topBar=new HBox(closeBtn);
            topBar.setAlignment(Pos.TOP_RIGHT);
            VBox content=new VBox(10,label,fileNameField,renameButton);
            closeBtn.getStyleClass().add("btn-cancel-transfer");
            VBox form = new VBox(5,topBar,content);
            form.setAlignment(Pos.TOP_CENTER);

            form.setMaxSize(300, 150);
            form.setPrefSize(300, 150);

            form.setStyle("-fx-background-color: white; " +
                    "-fx-padding: 20; " +
                    "-fx-background-radius: 5; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");

            overlay.getChildren().add(form);

            root.setCenter(overlay);

            renameButton.setOnAction(event->{

                //set the file name to the new file name
                Tab selected = tab.getSelectionModel().getSelectedItem();
                selected.getStyleClass().add("tabs");
                File file=(File) selected.getUserData();
                String name=fileNameField.getText().trim();

                if (file == null) {
                    new Alert(Alert.AlertType.WARNING,
                            "Please save the file before renaming.",
                            ButtonType.OK).showAndWait();
                    return;
                }
                if(name.isEmpty()) return;

                if(selected!=null && !name.isEmpty()){

                    try {
                        Path source = file.toPath();
                        Path target = source.resolveSibling(name);
                        Files.move(source, target);
                        File newFile = target.toFile();
                        selected.setUserData(newFile);
                        selected.setText(name);
                        //selected.setText(fileNameField.getText());



                    } catch (java.io.IOException ex) {
                        ex.printStackTrace();
                        Alert alert=new Alert(Alert.AlertType.WARNING,ex.getMessage(),ButtonType.OK);
                        alert.showAndWait();
                    }
                }

                root.setCenter(tab);

            });
            closeBtn.setOnAction(event->{
                root.setCenter(tab);
            });

        });



        cutitem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        copyitem.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        pasteitem.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        deleteitem.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        renameitem.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));


        readonlyitem.setOnAction(e->{
            TextArea text=getCurrentTextArea();

            Tab selectedTab=tab.getSelectionModel().getSelectedItem();
            File file=(File) selectedTab.getUserData();
            selectedTab.getStyleClass().add("tabs");
            if(selectedTab==null) return;
            if(file!=null){
                note.setAsReadOnly(file);
                selectedTab.setText(selectedTab.getText()+"[Read Only]");
            }else{
                Alert alert=new Alert(Alert.AlertType.WARNING,"Please save the file before setting is as read-only.",ButtonType.OK);
                alert.showAndWait();
            }
        });




        return menu;
    }



    public void start(Stage stage){
        TextArea initialArea = new TextArea();
        Tab initialTab = new Tab("Untitled", initialArea);
        addCloseHandler(initialTab);
        initialTab.setUserData(null);
        initialTab.getProperties().put("saved", true);
        tab.getTabs().add(initialTab);


        root.setTop(Appmenu(root));
        root.setCenter(tab);



        Scene scene=new Scene(root,800,600);
        stage.setScene(scene);
        scene.getStylesheets().add("NotePadUI.css");
        stage.setTitle("Notes");
        stage.show();
    }

    public static void main(String [] args){
        launch(args);
    }
}
