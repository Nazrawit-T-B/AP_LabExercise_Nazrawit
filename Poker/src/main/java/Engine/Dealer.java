package Engine;

import model.Card;
import model.Deck;
import model.Player;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    int Pot=30;
    public void initializeHand(List<Player> players, Deck deck){
        //function initializing the first set of cards for the robot and human Player
        for(int i=0;i<2;i++){
            for (Player player:players){
                player.getHand().add(deck.draw());
            }

        }
    }
    public List<Card> dealflop(Deck deck){
        List<Card> flops=new ArrayList<>();
        for(int i=0;i<3;i++){
            flops.add(deck.draw());
        }
        return flops;
    }
    public List<Card> turn(Deck deck){
        List<Card> t=new ArrayList<>();
        t.add(deck.draw());
        return t;
    }
    public List<Card> river(Deck deck){
        List<Card> r=new ArrayList<>();
        r.add(deck.draw());
        return r;
    }
}
