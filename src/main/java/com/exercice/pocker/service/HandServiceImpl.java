package com.exercice.pocker.service;

import com.exercice.pocker.model.Card;
import com.exercice.pocker.model.CardValue;
import com.exercice.pocker.model.Hand;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HandServiceImpl implements HandService {
    @Override
    public Boolean handCardDistinct(Hand hand) {
        //manuel search
        List<Card> cards = List.of(hand.card1(), hand.card2(), hand.card3(), hand.card4(), hand.card5());
        for (int i = 0; i < cards.size(); i++) {
            for (int j = i + 1; j < (cards.size()); j++) {
                if (cards.get(i).equals(cards.get(j)))
                    return false;
            }
        }
        return true;
        // *** java 8 style ***
        // return cards.stream().distinct().toList().size() == 5;
    }

    @Override
    public Boolean handsCardDistinct(List<Hand> hands) {
        List<Card> cards = hands.stream().flatMap(hand -> hand.getCards().stream()).toList();
        for (int i = 0; i < cards.size() - 1; i++) {
            for (int j = i + 1; j < cards.size(); j++) {
                if (cards.get(i).equals(cards.get(j))) {
                    return false;
                }
            }
        }
        return true;
        // *** java 8 style ***
        // return cards.stream().distinct().toList().size() == 5*hands.size();
    }

    @Override
    public Hand sortHandCardsAsc(Hand hand) {
        List<Card> cards = hand.getCards();
        List<Card> sorted = cards.stream()
                .sorted(Comparator.comparingInt(c -> c.getCardValue().getValue())).toList();
        return new Hand(hand.id(), sorted.get(0), sorted.get(1), sorted.get(2), sorted.get(3), sorted.get(4));
    }

    public Optional<Hand> getHandById(UUID id, List<Hand> hands) {
        return hands.stream()
                .filter(hand -> hand.id().equals(id))
                .findFirst();
    }

    @Override
    public Hand getHandWithHighestCard(List<Hand> hands) {
        Map<Hand, List<Card>> handCardsMap = hands.stream()
                .map(this::sortHandCardsAsc)
                .collect(Collectors.toMap(Function.identity(), Hand::getCards));
        while (handCardsMap.size() > 1 &&
                !handCardsMap.values().stream().flatMap(Collection::stream).toList().isEmpty()) {
            CardValue highestCardValue = handCardsMap.values().stream()
                    .map(List::getLast)
                    .max(Comparator.comparingInt(c -> c.getCardValue().getValue()))
                    .map(Card::getCardValue)
                    .get();
            handCardsMap = handCardsMap.entrySet().stream()
                    .filter(entry -> entry.getValue().getLast().getCardValue().equals(highestCardValue))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().stream()
                                    .filter(c -> !c.getCardValue().equals(highestCardValue))
                                    .toList()
                    ));
        }

        return handCardsMap.keySet().stream()
                .findFirst()
                .map(highestHand -> getHandById(highestHand.id(), hands).get())
                .orElseThrow();
    }

    @Override
    public List<CardValue> getHandPairsValue(Hand hand) {
        List<Card> sortedCards = sortHandCardsAsc(hand).getCards();
        List<CardValue> cardValuePair = new ArrayList<>();
        for (int i = 0; i < sortedCards.size() - 1; i++) {
            if (sortedCards.get(i).getCardValue().equals(sortedCards.get(i + 1).getCardValue())
                    && !cardValuePair.contains(sortedCards.get(i).getCardValue())) {
                CardValue cardValue = sortedCards.get(i).getCardValue();
                if (cardValuePair.isEmpty() || cardValuePair.getFirst().getValue() < cardValue.getValue()) {
                    cardValuePair.addFirst(cardValue);
                } else {
                    cardValuePair.addLast(cardValue);
                }
            }
        }
        return cardValuePair;
    }

    private Map<Hand, List<CardValue>> getHandsWithPairs(List<Hand> hands) {
        return hands.stream()
                .filter(h -> !getHandPairsValue(h).isEmpty())
                .collect(Collectors.toMap(Function.identity(), this::getHandPairsValue));
    }

    /**
     * <p>
     * process handPairMap to keep only hands with highest Pair
     * if many hands with same highest pair then move to the next highest pair
     * if all pairs are equal then return them all
     * </p>
     *
     * @param handPairMap
     * @return
     */
    private Map<Hand, List<CardValue>> processHighestPairMap(Map<Hand, List<CardValue>> handPairMap) {
        while (handPairMap.size() > 1 &&
                !handPairMap.values().stream().flatMap(Collection::stream).toList().isEmpty()) {
            CardValue highestPair = handPairMap.values().stream()
                    .map(List::getFirst)
                    .max(Comparator.comparingInt(CardValue::getValue)).get();
            handPairMap = handPairMap.entrySet().stream()
                    .filter(entry -> entry.getValue().getFirst().equals(highestPair))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().stream()
                                    .filter(cardValue -> !cardValue.equals(highestPair))
                                    .toList()
                    ));
        }
        return handPairMap;
    }

    private Optional<Hand> getHighestHandFromHandPairMap(List<Hand> hands, Map<Hand,
            List<CardValue>> handPairMap) {
        handPairMap = processHighestPairMap(handPairMap);
        boolean highestPairFound = handPairMap.size() == 1;
        if (highestPairFound) {
            return handPairMap.keySet().stream()
                    .findFirst()
                    .map(highestHand -> getHandById(highestHand.id(), hands).get());
        } else {
            return Optional.of(getHandWithHighestCard(hands));
        }
    }

    @Override
    public Optional<Hand> getHandWithHighestPair(List<Hand> hands) {
        Map<Hand, List<CardValue>> handPairMap = getHandsWithPairs(hands);
        return handPairMap.isEmpty() ? Optional.empty() : getHighestHandFromHandPairMap(hands, handPairMap);
    }

    private Map<Hand, List<CardValue>> getHandsWithTwoPairs(List<Hand> hands) {
        return hands.stream()
                .filter(h -> getHandPairsValue(h).size() > 1)
                .collect(Collectors.toMap(Function.identity(), this::getHandPairsValue));
    }

    @Override
    public Optional<Hand> getHandWithHighestTwoPair(List<Hand> hands) {
        Map<Hand, List<CardValue>> twoPairHandMap = getHandsWithTwoPairs(hands);
        return twoPairHandMap.isEmpty() ? Optional.empty() : getHighestHandFromHandPairMap(hands, twoPairHandMap);
    }

    private Optional<CardValue> getInputKindCardValue(Hand hand, int nbKind) {
        Assert.isTrue(nbKind <= 4 , "only twoKind, ThreeKind and FourKind are allowed");
        List<Card> sortedCardHand = sortHandCardsAsc(hand).getCards().reversed();
        return IntStream.range(0, 5-nbKind)
                .filter(i -> sortedCardHand.get(i).getCardValue().equals(sortedCardHand.get(i + 1).getCardValue())
                        && sortedCardHand.get(i + 1).getCardValue().equals(sortedCardHand.get(i + 2).getCardValue()))
                .mapToObj(sortedCardHand::get)
                .findFirst()
                .map(Card::getCardValue);
    }

    private Optional<CardValue> getThreeKindCardValue(Hand hand) {
        return getInputKindCardValue(hand, 3);
    }

    private Optional<Hand> getHandByCriteria(List<Hand> hands, Function<Hand,Optional<CardValue>> fn) {
        return hands.stream()
                .filter(hand -> fn.apply(hand).isPresent())
                .max(Comparator.comparingInt(hand -> fn.apply(hand).get().getValue()));
    }

    @Override
    public Optional<Hand> getHandWithHighestThreeOfKind(List<Hand> hands) {
        return getHandByCriteria(hands, this::getThreeKindCardValue);
    }

    private Optional<CardValue> getStraightHighestCardValue(Hand hand) {
        List<Card> sortedCard = sortHandCardsAsc(hand).getCards();
        OptionalInt firstDifferent = IntStream.range(0,4)
                .filter(i -> !sortedCard.get(i+1).getCardValue()
                        .equals(CardValue.fromValue(sortedCard.get(i).getCardValue().getValue()+1)))
                .findFirst();
        return firstDifferent.isEmpty() ?
                Optional.of(sortedCard.getLast().getCardValue()) :
                Optional.empty();
    }

    @Override
    public Optional<Hand> getHandWithHighestStraight(List<Hand> hands) {
        return getHandByCriteria(hands, this::getStraightHighestCardValue);
    }

    @Override
    public Optional<Hand> getHandWithHighestFlush(List<Hand> hands) {
        Predicate<Hand> isFlush = hand -> hand.getCards().stream()
                .allMatch(card -> card.getColor().equals(hand.card1().getColor()));
        List<Hand> handWithFlush = hands.stream().filter(isFlush).toList();
        return handWithFlush.isEmpty() ?
                Optional.empty() : Optional.of(getHandWithHighestCard(handWithFlush));

    }

    Optional<CardValue> getFullThreeKindCard(Hand hand) {
        Optional<CardValue> threeKindCardValue = getThreeKindCardValue(hand);
        if (threeKindCardValue.isPresent()) {
            Optional<CardValue> pairCardValue = getHandPairsValue(hand).stream()
                    .filter(pairCard -> !pairCard.equals(threeKindCardValue.get()))
                    .findFirst();
            return pairCardValue.isPresent() ? threeKindCardValue : Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Hand> getHandWithHighestFull(List<Hand> hands) {
        return getHandByCriteria(hands, this::getFullThreeKindCard);
    }

    private Optional<CardValue> getFourKindCardValue(Hand hand) {
        return getInputKindCardValue(hand, 4);
    }

    @Override
    public Optional<Hand> getHandWithHighestFourOfKind(List<Hand> hands) {
        return getHandByCriteria(hands, this::getFourKindCardValue);
    }

    private Optional<CardValue> getStraightFlushHighestCard(Hand hand) {
        Optional<CardValue> straightHighestCard = getStraightHighestCardValue(hand);
        if (straightHighestCard.isPresent()) {
            boolean allSameColor = hand.getCards().stream()
                    .allMatch(card -> card.getColor().equals(hand.card1().getColor()));
            return allSameColor ? straightHighestCard : Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Hand> getHandWithHighestStraightFlush(List<Hand> hands) {
        return getHandByCriteria(hands, this::getStraightFlushHighestCard);
    }

    @Override
    public Hand getHandWinningGame(List<Hand> hands) {
        if (!handsCardDistinct(hands)) {
            throw new InputMismatchException("Hands card must be distinct");
        }
        return getHandWithHighestStraightFlush(hands)
                .orElse(getHandWithHighestFourOfKind(hands)
                        .orElse(getHandWithHighestFull(hands)
                                .orElse(getHandWithHighestFlush(hands)
                                        .orElse(getHandWithHighestStraight(hands)
                                                .orElse(getHandWithHighestThreeOfKind(hands)
                                                        .orElse(getHandWithHighestTwoPair(hands)
                                                                .orElse(getHandWithHighestPair(hands)
                                                                        .orElse(getHandWithHighestCard(hands)))))))));
    }
}
