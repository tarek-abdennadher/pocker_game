package com.exercice.pocker.service;

import com.exercice.pocker.model.Card;
import com.exercice.pocker.model.CardValue;
import com.exercice.pocker.model.Color;
import com.exercice.pocker.model.Hand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class HandServiceTest {

    @Autowired
    private HandServiceImpl underTest;

    @Test
    void should_hand_has_no_null_card() {
        Card card1 = new Card(Color.C, CardValue.FIVE);
        Card card2 = new Card(Color.C, CardValue.FOUR);
        Card card3 = new Card(Color.C, CardValue.AS);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Hand validHand = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Exception notValidHand = Assertions.assertThrows(
                NullPointerException.class,
                ()->new Hand(UUID.randomUUID(),null,null,null,null,null)
        );

        Assertions.assertEquals(NullPointerException.class, notValidHand.getClass());
        Assertions.assertNotNull(validHand);
    }

    @Test
    void should_hand_contains_different_cards() {
        Card card1 = new Card(Color.C, CardValue.FIVE);
        Card card2 = new Card(Color.C, CardValue.FOUR);
        Card card3 = new Card(Color.C, CardValue.AS);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card card1Again = new Card(Color.C, CardValue.FIVE);

        Hand hand1 = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand hand2 = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card1Again);

        Assertions.assertEquals(Boolean.TRUE, underTest.handCardDistinct(hand1));
        Assertions.assertEquals(Boolean.FALSE, underTest.handCardDistinct(hand2));
    }

    @Test
    void should_hands_card_distinct() {
        Card card1_1 = new Card(Color.C, CardValue.FIVE);
        Card card1_2 = new Card(Color.C, CardValue.FOUR);
        Card card1_3 = new Card(Color.C, CardValue.AS);
        Card card1_4 = new Card(Color.C, CardValue.JACK);
        Card card1_5 = new Card(Color.C, CardValue.TEN);

        Card card2_1 = new Card(Color.P, CardValue.FIVE);
        Card card2_2 = new Card(Color.P, CardValue.FOUR);
        Card card2_3 = new Card(Color.P, CardValue.AS);
        Card card2_4 = new Card(Color.P, CardValue.JACK);
        Card card2_5 = new Card(Color.P, CardValue.TEN);

        Card card3_1 = new Card(Color.D, CardValue.FIVE);
        Card card3_2 = new Card(Color.D, CardValue.FOUR);
        Card card3_3 = new Card(Color.D, CardValue.AS);
        Card card3_4 = new Card(Color.P, CardValue.JACK);
        Card card3_5 = new Card(Color.D, CardValue.TEN);

        Hand hand1 = new Hand(UUID.randomUUID(),card1_1,card1_2,card1_3,card1_4,card1_5);
        Hand hand2 = new Hand(UUID.randomUUID(),card2_1,card2_2,card2_3,card2_4,card2_5);
        Hand hand3 = new Hand(UUID.randomUUID(),card3_1,card3_2,card3_3,card3_4,card3_5);

        Assertions.assertEquals(Boolean.TRUE, underTest.handsCardDistinct(List.of(hand1, hand2)));
        Assertions.assertEquals(Boolean.FALSE, underTest.handsCardDistinct(List.of(hand3, hand2)));
    }

    @Test
    void should_return_asc_sorted_hand() {
        Card card1 = new Card(Color.C, CardValue.FIVE);
        Card card2 = new Card(Color.C, CardValue.FOUR);
        Card card3 = new Card(Color.C, CardValue.AS);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        UUID uuid = UUID.randomUUID();
        Hand hand = new Hand(uuid,card1,card2,card3,card4,card5);
        Hand expectedHand = new Hand(uuid,card2,card1,card5,card4,card3);

        Hand actual = underTest.sortHandCardsAsc(hand);
        Assertions.assertEquals(expectedHand, actual);
    }

    @Test
    void should_return_hand_with_highest_card() {
        Card card1 = new Card(Color.C, CardValue.SIX);
        Card card2 = new Card(Color.C, CardValue.FIVE);
        Card card3 = new Card(Color.C, CardValue.AS);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card lowestCard = new Card(Color.C, CardValue.FOUR);

        Hand handHighest = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand hand2 = new Hand(UUID.randomUUID(),card1,lowestCard,card3,card4,card5);
        Hand hand3 = new Hand(UUID.randomUUID(),card1,lowestCard,card3,card4,card5);

        Assertions.assertEquals(Boolean.FALSE, underTest.getHandWithHighestCard(List.of(hand2,hand3)).isPresent());
        Assertions.assertEquals(handHighest, underTest.getHandWithHighestCard(List.of(handHighest,hand2)).get());
    }

    @Test
    void should_return_hand_cardValue_pairs_sorted_desc() {
        Card card1 = new Card(Color.C, CardValue.FIVE);
        Card card2 = new Card(Color.C, CardValue.FOUR);
        Card card3 = new Card(Color.C, CardValue.AS);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card cardPairJack = new Card(Color.D, CardValue.JACK);
        Card cardPairAs = new Card(Color.D, CardValue.AS);
        Card cardPairAs2 = new Card(Color.D, CardValue.AS);

        Hand handNoPair = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand handWithPairAs = new Hand(UUID.randomUUID(),cardPairAs,card2,card3,card4,cardPairAs2);
        Hand handWithPairJack = new Hand(UUID.randomUUID(),card1,card2,card3,card4,cardPairJack);
        Hand handWithTwoPair = new Hand(UUID.randomUUID(),cardPairAs,card2,card3,card4,cardPairJack);

        List<CardValue> expectAsPair = List.of(CardValue.AS);
        List<CardValue> expectJackPair = List.of(CardValue.JACK);
        List<CardValue> expectTwoPairSorted = List.of(CardValue.AS, CardValue.JACK);

        Assertions.assertEquals(List.of(), underTest.getHandPairsValue(handNoPair));
        Assertions.assertEquals(expectJackPair, underTest.getHandPairsValue(handWithPairJack));
        Assertions.assertEquals(expectAsPair, underTest.getHandPairsValue(handWithPairAs));
        Assertions.assertEquals(expectTwoPairSorted, underTest.getHandPairsValue(handWithTwoPair));
    }

    @Test
    void should_return_hand_with_highest_pair() {
        Card card1 = new Card(Color.C, CardValue.FIVE);
        Card card2 = new Card(Color.C, CardValue.FOUR);
        Card card3 = new Card(Color.C, CardValue.AS);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card cardPairJack = new Card(Color.D, CardValue.JACK);
        Card cardPairAs = new Card(Color.D, CardValue.AS);

        Hand handNoPair = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand handWithPairAs = new Hand(UUID.randomUUID(),cardPairAs,card2,card3,card4,card5);
        Hand handWithPairJack = new Hand(UUID.randomUUID(),card1,card2,card3,card4,cardPairJack);

        Optional<Hand> actualHandWithAs = underTest.getHandWithHighestPair(List.of(handNoPair, handWithPairJack, handWithPairAs));
        Assertions.assertEquals(Boolean.TRUE, actualHandWithAs.isPresent());
        Assertions.assertEquals(handWithPairAs, actualHandWithAs.get());

        Optional<Hand> actualHandWithJack = underTest.getHandWithHighestPair(List.of(handNoPair, handWithPairJack));
        Assertions.assertEquals(Boolean.TRUE, actualHandWithJack.isPresent());
        Assertions.assertEquals(handWithPairJack, actualHandWithJack.get());

        Optional<Hand> actualHandWithoutPair = underTest.getHandWithHighestPair(List.of(handNoPair, handNoPair));
        Assertions.assertEquals(Boolean.FALSE, actualHandWithoutPair.isPresent());
    }

    @Test
    void should_return_hand_with_highest_two_pair() {
        Card card1 = new Card(Color.C, CardValue.FIVE);
        Card card2 = new Card(Color.C, CardValue.FOUR);
        Card card3 = new Card(Color.C, CardValue.AS);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card cardPairJack = new Card(Color.D, CardValue.JACK);
        Card cardPairAs = new Card(Color.D, CardValue.AS);
        Card cardPairTen = new Card(Color.D, CardValue.TEN);

        Hand handNoPair = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand handWithPairAs = new Hand(UUID.randomUUID(),cardPairAs,card2,card3,card4,card5);
        Hand handPairAsJackAndTen = new Hand(UUID.randomUUID(),cardPairAs,cardPairJack,card3,card4,card5);
        Hand handPairAsJackAndFive = new Hand(UUID.randomUUID(),cardPairAs,cardPairJack,card3,card4,card1);
        Hand handPairAsTen = new Hand(UUID.randomUUID(),cardPairAs,cardPairTen,card3,card4,card5);
        Hand handPairJackTen = new Hand(UUID.randomUUID(),cardPairJack,cardPairTen,card3,card4,card5);

        Optional<Hand> actualNoTwoPair = underTest.getHandWithHighestTwoPair(List.of(handNoPair, handWithPairAs));
        Assertions.assertEquals(Boolean.FALSE, actualNoTwoPair.isPresent());

        // case same two pair and highest card
        Optional<Hand> actualAsJackPairTen = underTest.getHandWithHighestTwoPair(List.of(handPairAsJackAndTen, handPairAsJackAndFive));
        Assertions.assertEquals(Boolean.TRUE, actualAsJackPairTen.isPresent());
        Assertions.assertEquals(handPairAsJackAndTen, actualAsJackPairTen.get());

        // case the highest pair equal
        Optional<Hand> actualAsJackPair = underTest.getHandWithHighestTwoPair(List.of(handPairAsJackAndFive, handPairAsTen));
        Assertions.assertEquals(Boolean.TRUE, actualAsJackPair.isPresent());
        Assertions.assertEquals(handPairAsJackAndFive, actualAsJackPair.get());

        // case the highest pair not equal
        Optional<Hand> actualAsTenPair = underTest.getHandWithHighestTwoPair(List.of(handPairAsTen, handPairJackTen));
        Assertions.assertEquals(Boolean.TRUE, actualAsTenPair.isPresent());
        Assertions.assertEquals(handPairAsTen, actualAsTenPair.get());
    }

    @Test
    void should_return_hand_with_highest_three_of_kind() {
        Card card1 = new Card(Color.C, CardValue.FIVE);
        Card card2 = new Card(Color.C, CardValue.FOUR);
        Card card3 = new Card(Color.C, CardValue.AS);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card cardJack2 = new Card(Color.D, CardValue.JACK);
        Card cardJack3 = new Card(Color.T, CardValue.JACK);
        Card cardTen2 = new Card(Color.D, CardValue.TEN);
        Card cardTen3 = new Card(Color.D, CardValue.TEN);

        Hand handNoThree = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand handThreeOfJack = new Hand(UUID.randomUUID(),card1,cardJack2,cardJack3,card4,card5);
        Hand handThreeOfTen = new Hand(UUID.randomUUID(),card1,cardTen2,cardTen3,card4,card5);

        Optional<Hand> actualNoThreeOfKind = underTest.getHandWithHighestThreeOfKind(List.of(handNoThree));
        Assertions.assertEquals(Boolean.FALSE, actualNoThreeOfKind.isPresent());

        Optional<Hand> actualThreeOfJack = underTest.getHandWithHighestThreeOfKind(List.of(handThreeOfJack, handThreeOfTen));
        Assertions.assertEquals(Boolean.TRUE, actualThreeOfJack.isPresent());
        Assertions.assertEquals(handThreeOfJack, actualThreeOfJack.get());
    }

    @Test
    void should_return_hand_with_highest_straight() {
        Card card1 = new Card(Color.C, CardValue.EIGHT);
        Card card2 = new Card(Color.P, CardValue.SEVEN);
        Card card3 = new Card(Color.C, CardValue.NINE);
        Card card4 = new Card(Color.T, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card cardQueen = new Card(Color.D, CardValue.QUEEN);
        Card cardFour = new Card(Color.C, CardValue.FOUR);

        Hand handNoStraight = new Hand(UUID.randomUUID(),card1,card2,card3,cardFour,card5);
        Hand handStraightJack = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand handStraightQueen = new Hand(UUID.randomUUID(),card1,cardQueen,card3,card4,card5);

        Optional<Hand> actualNoStraight = underTest.getHandWithHighestStraight(List.of(handNoStraight));
        Assertions.assertEquals(Boolean.FALSE, actualNoStraight.isPresent());

        Optional<Hand> actualStraightOfQueen = underTest.getHandWithHighestStraight(List.of(handStraightQueen, handStraightJack));
        Assertions.assertEquals(Boolean.TRUE, actualStraightOfQueen.isPresent());
        Assertions.assertEquals(handStraightQueen, actualStraightOfQueen.get());
    }

    @Test
    void should_return_hand_with_highest_Flush() {
        Card card1 = new Card(Color.C, CardValue.FIVE);
        Card card2 = new Card(Color.C, CardValue.FOUR);
        Card card3 = new Card(Color.C, CardValue.AS);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card card1_2 = new Card(Color.T, CardValue.FIVE);
        Card card2_2 = new Card(Color.T, CardValue.FOUR);
        Card card3_2 = new Card(Color.T, CardValue.KING);
        Card card4_2 = new Card(Color.T, CardValue.JACK);
        Card card5_2 = new Card(Color.T, CardValue.TEN);

        Hand handNoFlush = new Hand(UUID.randomUUID(),card1,card2,card3,card4_2,card5);
        Hand handFlushAs = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand handFlushKing = new Hand(UUID.randomUUID(),card1_2,card2_2,card3_2,card4_2,card5_2);

        Optional<Hand> actualNoFlush = underTest.getHandWithHighestFlush(List.of(handNoFlush));
        Assertions.assertEquals(Boolean.FALSE, actualNoFlush.isPresent());

        Optional<Hand> actualFlushAs = underTest.getHandWithHighestFlush(List.of(handFlushKing, handFlushAs));
        Assertions.assertEquals(Boolean.TRUE, actualFlushAs.isPresent());
        Assertions.assertEquals(handFlushAs, actualFlushAs.get());
    }

    @Test
    void should_return_hand_with_highest_full() {
        Card card1 = new Card(Color.C, CardValue.FIVE);
        Card card2 = new Card(Color.P, CardValue.FIVE);
        Card card3 = new Card(Color.C, CardValue.JACK);
        Card card4 = new Card(Color.T, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card cardFive = new Card(Color.T, CardValue.FIVE);
        Card cardJack = new Card(Color.P, CardValue.JACK);

        Hand handNoFull = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand handFullFive = new Hand(UUID.randomUUID(),card1,card2,card3,card4,cardFive);
        Hand handFullJack = new Hand(UUID.randomUUID(),card1,card2,card3,card4,cardJack);

        Optional<Hand> actualNoFull = underTest.getHandWithHighestFull(List.of(handNoFull));
        Assertions.assertEquals(Boolean.FALSE, actualNoFull.isPresent());

        Optional<Hand> actualJackFull = underTest.getHandWithHighestFull(List.of(handFullFive, handFullJack));
        Assertions.assertEquals(Boolean.TRUE, actualJackFull.isPresent());
        Assertions.assertEquals(handFullJack, actualJackFull.get());
    }

    @Test
    void should_return_hand_with_highest_four_of_kind() {
        Card card1 = new Card(Color.C, CardValue.FIVE);
        Card card2 = new Card(Color.P, CardValue.FIVE);
        Card card3 = new Card(Color.D, CardValue.FIVE);
        Card card4 = new Card(Color.T, CardValue.JACK);
        Card card5 = new Card(Color.D, CardValue.JACK);
        Card cardFive = new Card(Color.T, CardValue.FIVE);
        Card cardJackP = new Card(Color.P, CardValue.JACK);
        Card cardJackC = new Card(Color.C, CardValue.JACK);

        Hand handNoFour = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand handFourOfFive = new Hand(UUID.randomUUID(),card1,card2,card3,cardFive,card5);
        Hand handFourOfJack = new Hand(UUID.randomUUID(),card1,cardJackC,cardJackP,card4,card5);

        Optional<Hand> actualNoFourOfKind = underTest.getHandWithHighestFourOfKind(List.of(handNoFour));
        Assertions.assertEquals(Boolean.FALSE, actualNoFourOfKind.isPresent());

        Optional<Hand> actualForOfJack = underTest.getHandWithHighestFourOfKind(List.of(handFourOfFive, handFourOfJack));
        Assertions.assertEquals(Boolean.TRUE, actualForOfJack.isPresent());
        Assertions.assertEquals(handFourOfJack, actualForOfJack.get());
    }

    @Test
    void should_return_hand_with_highest_straight_flush() {
        Card card1 = new Card(Color.C, CardValue.EIGHT);
        Card card2 = new Card(Color.C, CardValue.SEVEN);
        Card card3 = new Card(Color.C, CardValue.NINE);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card cardQueen = new Card(Color.C, CardValue.QUEEN);
        Card cardFour = new Card(Color.C, CardValue.FOUR);
        Card card1_2 = new Card(Color.T, CardValue.EIGHT);

        Hand handNoStraight = new Hand(UUID.randomUUID(),card1,card2,card3,cardFour,card5);
        Hand handNoStraightFlush = new Hand(UUID.randomUUID(),card1_2,card2,card3,card4,card5);
        Hand handStraightFlushJack = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand handStraightFlushQueen = new Hand(UUID.randomUUID(),card1,cardQueen,card3,card4,card5);

        Optional<Hand> actualNoStraight = underTest.getHandWithHighestStraightFlush(List.of(handNoStraight, handNoStraightFlush));
        Assertions.assertEquals(Boolean.FALSE, actualNoStraight.isPresent());

        Optional<Hand> actualStraightFlushOfQueen = underTest.getHandWithHighestStraightFlush(List.of(handStraightFlushJack, handStraightFlushQueen));
        Assertions.assertEquals(Boolean.TRUE, actualStraightFlushOfQueen.isPresent());
        Assertions.assertEquals(handStraightFlushQueen, actualStraightFlushOfQueen.get());
    }

    @Test
    void should_hand_with_highest_card_win_the_game() {
        Card card1 = new Card(Color.C, CardValue.SIX);
        Card card2 = new Card(Color.C, CardValue.FIVE);
        Card card3 = new Card(Color.C, CardValue.AS);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card card2_1 = new Card(Color.P, CardValue.FIVE);
        Card card2_2 = new Card(Color.P, CardValue.FOUR);
        Card card2_3 = new Card(Color.P, CardValue.AS);
        Card card2_4 = new Card(Color.P, CardValue.JACK);
        Card card2_5 = new Card(Color.P, CardValue.TEN);

        Hand handHighest = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand hand2 = new Hand(UUID.randomUUID(),card2_1,card2_2,card2_3,card2_4,card2_5);

        String expectedWinner = String.format("Player with id %s is the winner", handHighest.id());

        Assertions.assertEquals(expectedWinner, underTest.getHandWinningGame(List.of(handHighest,hand2)));
    }

    @Test
    void should_return_draw_for_straight_flush() {
        Card card1 = new Card(Color.C, CardValue.EIGHT);
        Card card2 = new Card(Color.C, CardValue.SEVEN);
        Card card3 = new Card(Color.C, CardValue.NINE);
        Card card4 = new Card(Color.C, CardValue.JACK);
        Card card5 = new Card(Color.C, CardValue.TEN);
        Card card2_1 = new Card(Color.P, CardValue.EIGHT);
        Card card2_2 = new Card(Color.P, CardValue.SEVEN);
        Card card2_3 = new Card(Color.P, CardValue.NINE);
        Card card2_4 = new Card(Color.P, CardValue.JACK);
        Card card2_5 = new Card(Color.P, CardValue.TEN);

        Hand hand1 = new Hand(UUID.randomUUID(),card1,card2,card3,card4,card5);
        Hand hand2 = new Hand(UUID.randomUUID(),card2_1,card2_2,card2_3,card2_4,card2_5);

        String expectedResult = "draw";

        Assertions.assertEquals(expectedResult, underTest.getHandWinningGame(List.of(hand1,hand2)));
    }

}
