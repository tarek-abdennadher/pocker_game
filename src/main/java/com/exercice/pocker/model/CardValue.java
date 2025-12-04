package com.exercice.pocker.model;

public enum CardValue {
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13),
    AS(14);

    private int value;

    CardValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static CardValue fromValue(int value) {
        for (CardValue cv : CardValue.values()) {
            if (cv.value == value) {
                return cv;
            }
        }
        throw new IllegalArgumentException("No CardValue with value: " + value);
    }
}
