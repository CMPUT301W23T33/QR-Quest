package com.example.qrquest;

import org.junit.Test;

import static org.junit.Assert.*;

public class RankTest {
    Rank rank = new Rank();

    String identifier = "Identifier #1";
    int score = 2506;

    // if created successfully, then setIdentifier and setScore work
    Rank rank1 = new Rank(identifier, score);

    @Test
    public void getIdentifierTest() {
        assertEquals(rank.getIdentifier(), "");
        assertNotNull(rank1.getIdentifier());
        assertEquals(rank1.getIdentifier(), identifier);
    }

    @Test
    public void getValueTest() {
        assertEquals(0, rank.getValue());
        assertNotEquals(0, rank1.getValue());
        assertEquals(score, rank1.getValue());
    }

    @Test
    public void getSetRankTest() {
        int rankValue = 1;
        assertEquals(0, rank.getRank());
        rank.setRank(rankValue);
        assertEquals(rankValue, rank.getRank());
    }



}
