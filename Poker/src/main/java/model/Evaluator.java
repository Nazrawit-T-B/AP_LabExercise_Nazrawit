package model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Evaluator {
    public static HandRankings evaluate(List<Card> handCards,List<Card> community){
        List<Card> allCards=new ArrayList<>();
        allCards.addAll(handCards);
        allCards.addAll(community);

        if (isRoyalFlush(allCards))       return HandRankings.ROYAL_FLUSH;
        if (isStraightFlush(allCards))    return HandRankings.STRAIGHT_FLUSH;
        if (isFourOfAKind(allCards))      return HandRankings.FOUR_OF_A_KIND;
        if (isFullHouse(allCards))        return HandRankings.FULL_HOUSE;
        if (isFlush(allCards))            return HandRankings.FLUSH;
        if (isStraight(allCards))         return HandRankings.STRAIGHT;
        if (isThreeOfAKind(allCards))     return HandRankings.THREE_OF_A_KIND;
        if (isTwoPair(allCards))          return HandRankings.TWO_PAIR;
        if (isOnePair(allCards))          return HandRankings.ONE_PAIR;
        return HandRankings.HIGH_CARD;
    }
    private static boolean isRoyalFlush(List<Card> cards) {
        //Royal Flush is 4 cards that are same suit with orders kept and the highest card is the Ace
        return isStraightFlush(cards) && getHighCard(cards).getRank() == Rank.ACE;
    }

    private static boolean isStraightFlush(List<Card> cards) {
        //isFlush to check if they are in the same suit
        //isStraight to check if they are consecutive
        return isFlush(cards) && isStraight(cards);
    }

    private static boolean isFourOfAKind(List<Card> cards) {
        //check if all the grouped cards have 4 cards in them and return true if that is the case
        return getGroupCounts(cards).containsValue(4L);
    }

    private static boolean isFullHouse(List<Card> cards) {
        Map<Rank, Long> counts = getGroupCounts(cards);
        return counts.containsValue(3L) && counts.containsValue(2L);
    }

    private static boolean isFlush(List<Card> cards) {
        //five cards in the same suit , consecutiveness doesn't matter
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()))
                .values().stream()
                .anyMatch(count -> count >= 5);
    }

    private static boolean isStraight(List<Card> cards) {
        //consecutive ,suit doesn't matter
        List<Integer> values = cards.stream()
                .map(c -> c.getRank().getValue())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        for (int i = 0; i <= values.size() - 5; i++) {
            if (values.get(i + 4) - values.get(i) == 4) return true;
        }
        return false;
    }

    private static boolean isThreeOfAKind(List<Card> cards) {
        return getGroupCounts(cards).containsValue(3L);
    }

    private static boolean isTwoPair(List<Card> cards) {
        long pairs = getGroupCounts(cards).values().stream().filter(v -> v == 2).count();
        return pairs >= 2;
    }

    private static boolean isOnePair(List<Card> cards) {
        return getGroupCounts(cards).containsValue(2L);
    }

    //I haven't fully understood this, like what is this?????

    private static Map<Rank, Long> getGroupCounts(List<Card> cards) {
        //how many of each rank do I have

        //groupingBy(Card::getRank) - groups cards together that share the same rank

        //Collectors.counting()- counts how many cards there are in each group

        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));
    }

    private static Card getHighCard(List<Card> cards) {
        //which card has the highest rank value
        //c->c.getRank().getValue()- for each card in cards get its Rank first and then its rank value
        return cards.stream()
                .max(Comparator.comparingInt(c -> c.getRank().getValue()))
                .orElseThrow();
    }
}
