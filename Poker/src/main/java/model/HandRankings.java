package model;

public enum HandRankings {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    STRAIGHT,
    FLUSH,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    STRAIGHT_FLUSH,
    ROYAL_FLUSH;

    public int getRank() {
        return this.ordinal(); // Higher ordinal = stronger hand
        //ordinal is a built-in Java enum method that returns the position of the constant starting from 0 as indexes in arrays.
    }
}