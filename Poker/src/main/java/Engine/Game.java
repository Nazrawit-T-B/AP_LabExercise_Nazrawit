package Engine;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    Dealer dealer=new Dealer();
    private Robot robot;
    Deck deck=new Deck();
    List<Player>players=new ArrayList<>();
    List<List<Card>> community=new ArrayList<>();
    int round=0;

    public Game(){
        this.dealer=dealer;
        this.deck=deck;
        this.players=players;
    }

    public int setupGame(){
        Player human=new Player();
        robot=new Robot();

        players.add(human);
        players.add(robot);
        return round;
    }
    public int startGame(){
        dealer.initializeHand(players,deck);
        int value=dealer.Pot;
        return value;
    }
    public List<List<Card>> showHands() {
        List<List<Card>> hands=new ArrayList<>();
        for (Player p : players) {
            System.out.println(p.getHand());
            hands.add(p.getHand());
        }
        return hands;
    }
    public List<Card> showFlop(){
        List<Card> flop=dealer.dealflop(deck);
        community.add(flop);
        return flop;
    }
    public List<Card> showTurn(){
        List<Card> turn=dealer.turn(deck);
        community.add(turn);
        return turn;
    }
    public List<Card> showRiver(){
        List<Card> river=dealer.river(deck);
        community.add(river);
        return river;
    }

    public String robotDecision(){
        String decision=robot.makeDecision();
        return decision;
    }

     public int decisionResult(String decision){
         Random rand=new Random();
        int result=0;
        if(decision.equals("Fold")){
            result=0;
        } else if (decision.equals("Call")) {
            result=20;
        } else if (decision.equals("Raise")) {
            int []numoptions={50,100,500,1000,5000,10000};
            result=numoptions[rand.nextInt(numoptions.length)];
        }
        return result;
    }
    public String decideWinner(List<Card> Player,List<Card> Robot,List<Card> Community){

        //A card with higher heirarchy wins

        HandRankings playerHand = Evaluator.evaluate(Player, Community);
        HandRankings robotHand  = Evaluator.evaluate(Robot, Community);

        int cmp = Integer.compare(playerHand.getRank(), robotHand.getRank());

        if (cmp > 0) return " Player- "+playerHand;
        if (cmp < 0) return "Robot- "+ robotHand;
        return "Draw";
    }

}
