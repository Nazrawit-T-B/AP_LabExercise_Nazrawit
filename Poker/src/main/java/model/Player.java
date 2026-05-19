package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    //what are the attributes of the player
    //1. Has a hand of card
    //2. Can make decisions
    private List<Card> hand=new ArrayList<>();
    public List<Card> getHand(){
        return hand;
    }
    public String makeDecision(){
        String decision="";
        return decision;
    }
}
