package model;

import java.util.Random;

public class Robot extends Player{
    String [] options={"Call","Raise"};
    Random random=new Random();

    @Override

    public String makeDecision(){

        String decision="";
        int index= random.nextInt(options.length);
        decision=options[index];
        return decision;

    }
   

}
