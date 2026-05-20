package com.app;

import Engine.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Card;


import java.util.*;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    BorderPane root =new BorderPane();
    StackPane stackRoot = new StackPane();
    Game game=new Game();
    VBox buttons=new VBox(10);
    HBox gameButtons=new HBox(3);
    HBox programButtons=new HBox(10);

    Button fold=new Button("Fold");
    Button call=new Button("Call 20");
    Button raise=new Button("Raise 50");
    Button exit=new Button("End Game");
    Button play=new Button("Play Again");

    HBox robotCards=new HBox(10);
    HBox humanCards=new HBox(10);
    HBox flopBox = new HBox(10);
    HBox community=new HBox(10);
    HBox turnBox=new HBox(10);
    HBox riverBox=new HBox(10);
    VBox chips=new VBox(-20);
    HBox pot=new HBox(10);
    HBox initChips=new HBox(-20);
    VBox topsection=new VBox(3);
    VBox centerBox = new VBox(25);
    Label label=new Label();
    Label robotActionlabel=new Label();
    int round=0;
    int value=0;
    int robotValue=0;
    String robotAction="";

    ComboBox<Integer> raiseMenu;// add this field
    Label winnermessage=new Label();
    boolean isFolded=false;

    List<Card> communitycard=new ArrayList<>();
    List<Card> flops=new ArrayList<>();
    List<Card> turn=new ArrayList<>();
    List<Card> river=new ArrayList<>();
    List<Card> human =new ArrayList<>();
    List<Card> robot=new ArrayList<>();

    private ImageView getCardImage(Card card) {
        // Map suit enum to filename prefix
        String suit = switch (card.getSuit()) {
            case CLUBS -> "club";
            case HEARTS -> "heart";
            case DIAMONDS -> "diamond";
            case SPADES -> "spade";
        };

        // Map rank enum to 4-digit number
        String rank = switch (card.getRank()) {
            case ACE -> "0001";
            case TWO -> "0002";
            case THREE -> "0003";
            case FOUR -> "0004";
            case FIVE -> "0005";
            case SIX -> "0006";
            case SEVEN -> "0007";
            case EIGHT -> "0008";
            case NINE -> "0009";
            case TEN -> "0010";
            case JACK -> "0011";
            case QUEEN -> "0012";
            case KING -> "0013";
        };

        // Build the full path
        String path = "/" + suit + "_" + rank + ".png";

        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView view = new ImageView(image);
        view.setFitWidth(80);
        view.setFitHeight(120);
        return view;

    }
    public ImageView getCardBack(){
        Image image = new Image(getClass().getResourceAsStream("/card_back.png"));
        ImageView view = new ImageView(image);
        view.setFitWidth(80);
        view.setFitHeight(120);
        return view;
    }
    public ImageView getCallChip(){
        Image image=new Image(getClass().getResourceAsStream("/chip20.png"));
        ImageView view=new ImageView(image);
        view.setFitWidth(40);
        view.setFitHeight(40);
        return view;
    }
    public ImageView getPotChip(){
        Image image=new Image(getClass().getResourceAsStream("/chip10.png"));
        ImageView view=new ImageView(image);
        view.setFitWidth(40);
        view.setFitHeight(40);
        return view;
    }
    public ImageView getRaiseChip(int value){
        String chipFile;
        switch(value){
            case 50:
                chipFile="/chip50.png";
                break;
            case 100:
                chipFile="/chip100.png";
                break;
            case 500:
                chipFile="/chip500.png";
                break;
            case 1000:
                chipFile="/chip1000.png";
                break;
            case 5000:
                chipFile="/chip5000.png";
                break;
            case 10000:
                chipFile="/chip10000.png";
                break;
            default:
                chipFile="/chip50.png";
        }

        Image image = new Image(getClass().getResourceAsStream(chipFile));
        ImageView view = new ImageView(image);
        view.setFitWidth(40);
        view.setFitHeight(40);
        return view;
    }
    private void progressRound(int betAmount, Label label,VBox topSection) {
        if (round == 0) {
            flops = game.showFlop();
            flopBox.getChildren().clear();
            flopBox.getChildren().addAll(
                    getCardImage(flops.get(0)),
                    getCardImage(flops.get(1)),
                    getCardImage(flops.get(2)));
            communitycard.add(flops.get(0));
            communitycard.add(flops.get(1));
            communitycard.add(flops.get(2));

        } else if (round == 1) {
            turn = game.showTurn();
            turnBox.getChildren().clear();
            turnBox.getChildren().add(getCardImage(turn.getFirst()));
            communitycard.add(turn.getFirst());

        } else if (round == 2) {
            river = game.showRiver();
            riverBox.getChildren().clear();
            riverBox.getChildren().add(getCardImage(river.getFirst()));
            communitycard.add(river.getFirst());

        }

        // shared betting logic for rounds 0-2
        chips.getChildren().add(betAmount == 20 ? getCallChip() : getRaiseChip(betAmount));
        robotAction = game.robotDecision();
        robotActionlabel.setText("Robot does- "+ robotAction);
        System.out.println(robotAction);
        robotValue = game.decisionResult(robotAction);

        if (robotValue == 0) {
            winnermessage.setText("Robot folded,Player wins the Pot");
            isFolded = true;
            round = 3;
        }

        value = value + betAmount + robotValue;
        label.setText("Pot: " + value);
        round++;

        if(round==3 && !isFolded){
            //reveal the robot hands here
            robotCards.getChildren().clear();
            robotCards.getChildren().addAll(getCardImage(robot.getFirst()),getCardImage(robot.getLast()));
            winnermessage.setText(game.decideWinner(human, robot, communitycard));
        }
    }
    public void start(Stage stage){



        //setup
        round=game.setupGame();
        
        //start
         value=game.startGame();
         human =game.showHands().get(0);
        robot=game.showHands().get(1);
        robotCards.getChildren().addAll(getCardBack(),getCardBack());
        robotCards.setPadding(new Insets(40));
        robotCards.setAlignment(Pos.CENTER);
        humanCards.getChildren().addAll(getCardImage(human.get(0)),getCardImage(human.get(1)));

        label.setText("POT:"+ value);
        label.setStyle("-fx-text-fill:white;"+ "-fx-font-family: \"Consolas\";"+"-fx-font-size:18;");
        robotActionlabel.setStyle("-fx-text-fill:white;"+ "-fx-font-family: \"Consolas\";"+"-fx-font-size:16;");
        winnermessage.setStyle("-fx-text-fill:green;"+ "-fx-font-family: \"Consolas\";"+"-fx-font-size:30;");

        fold.getStyleClass().add("btn-fold");
        call.getStyleClass().add("btn-call");
        raise.getStyleClass().add("btn-raise");
        exit.getStyleClass().add("btn-exit");
        play.getStyleClass().add("btn-play");

        pot.getChildren().addAll(label,initChips);
        pot.setAlignment(Pos.CENTER);
        initChips.getChildren().addAll(getPotChip(),getPotChip(),getPotChip());

        topsection.getChildren().addAll(robotCards,pot,robotActionlabel,winnermessage);

        topsection.setAlignment(Pos.TOP_CENTER);
        humanCards.setAlignment(Pos.CENTER);
        community.getChildren().addAll(flopBox,turnBox,riverBox);
        community.setAlignment(Pos.CENTER);
        chips.setPadding(new Insets(0,0,0,40));

        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(community, humanCards);
        flopBox.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(gameButtons,programButtons);
        buttons.setAlignment(Pos.CENTER);
        programButtons.getChildren().addAll(exit,play);
        programButtons.setAlignment(Pos.CENTER);
        programButtons.setPadding(new Insets(10,0,30,0));
        gameButtons.getChildren().addAll(fold,call,raise,raisemenu());
        gameButtons.setPadding(new Insets(0,0,10,0));
        gameButtons.setAlignment(Pos.CENTER);

        fold.setOnAction(e->{
            if(round<3) {
                round = 3;
                winnermessage.setText("Player Loses the Pot");
                winnermessage.setStyle("-fx-text-fill:red;" + "-fx-font-family: \"Consolas\";" + "-fx-font-size:30;");
                topsection.getChildren().add(winnermessage);
                isFolded = true;
            }
        });

        raise.setOnAction(e->{
            progressRound(raiseMenu.getValue(),label,topsection);
        });
        call.setOnAction(e->{
            progressRound(20,label,topsection);
        });
        exit.setOnAction(e->{

            Platform.exit();
            System.exit(0);

        });
        play.setOnAction(e -> {
            // reset state
            round = 0;
            value = 0;
            robotValue = 0;
            isFolded = false;
            communitycard.clear();
            flops.clear();
            turn.clear();
            river.clear();

            // close current window and open a fresh one
            Stage newStage = new Stage();
            stage.close();
            new Main().start(newStage);
        });

        root.setTop(topsection);
        root.setCenter(centerBox);
        root.setLeft(chips);
        root.setBottom(buttons);
        BorderPane.setAlignment(topsection,Pos.CENTER);
        BorderPane.setAlignment(centerBox,Pos.CENTER);
        Scene scene=new Scene(root,900,800);
        scene.getStylesheets().add("PokerUI.css");
        stage.setTitle("Poker");
        stage.setScene(scene);
        stage.show();
    }
    public ComboBox<Integer> raisemenu(){
        ObservableList<Integer> options= FXCollections.observableArrayList(25,50,100,500,1000,5000,10000);
        raiseMenu=new ComboBox<>(options);
        raiseMenu.setValue(50);
        raiseMenu.valueProperty().addListener((ob,old,nw)->
                raise.setText("Raise "+ nw));
        return raiseMenu;
    }
    public static void main(String[] args){
        launch(args);
    }
}