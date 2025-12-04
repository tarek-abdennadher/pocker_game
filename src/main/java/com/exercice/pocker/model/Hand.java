package com.exercice.pocker.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Hand(UUID id, Card card1, Card card2, Card card3, Card card4, Card card5) {
    public Hand{
        Objects.requireNonNull(id);
        Objects.requireNonNull(card1);
        Objects.requireNonNull(card2);
        Objects.requireNonNull(card3);
        Objects.requireNonNull(card4);
        Objects.requireNonNull(card5);
    }

    public List<Card> getCards() {
        return List.of(this.card1(), this.card2(), this.card3(), this.card4(), this.card5());
    }
}
