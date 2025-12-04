package com.exercice.pocker.service;

import com.exercice.pocker.model.Card;
import com.exercice.pocker.model.CardValue;
import com.exercice.pocker.model.Hand;

import java.util.List;
import java.util.Optional;

public interface HandService {

    Boolean handCardDistinct(Hand hand);
    Boolean handsCardDistinct(List<Hand> hands);
    Hand sortHandCardsAsc(Hand hand);
    Hand getHandWithHighestCard(List<Hand> hands);
    List<CardValue> getHandPairsValue(Hand hand);
    Optional<Hand> getHandWithHighestPair(List<Hand> hands);
    Optional<Hand> getHandWithHighestTwoPair(List<Hand> hands);
    Optional<Hand> getHandWithHighestThreeOfKind(List<Hand> hands);
    Optional<Hand> getHandWithHighestStraight(List<Hand> hands);
    Optional<Hand> getHandWithHighestFlush(List<Hand> hands);
    Optional<Hand> getHandWithHighestFull(List<Hand> hands);
    Optional<Hand> getHandWithHighestFourOfKind(List<Hand> hands);
    Optional<Hand> getHandWithHighestStraightFlush(List<Hand> hands);
    Hand getHandWinningGame(List<Hand> hands);
}
