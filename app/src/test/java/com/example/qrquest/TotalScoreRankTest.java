package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests class TotalScoreRank
 * @author Dang Viet Anh Dinh
 */
public class TotalScoreRankTest {

    private String identifier1, identifier2, identifier3, identifier4;
    private Integer value1, value2, value3, value4;
    private TotalScoreRank totalScoreRank1, totalScoreRank2,
            totalScoreRank3, totalScoreRank4;

    @Before
    public void setup() {
        this.identifier1 = "Player 1";
        this.identifier2 = "Player 2";
        this.identifier3 = "Player 3";
        this.identifier4 = "Player 4";
        this.value1 = 100000;
        this.value2 = 10000;
        this.value3 = 100;
        this.value4 = 100;
        this.totalScoreRank1 = new TotalScoreRank(this.identifier1, this.value1);
        this.totalScoreRank2 = new TotalScoreRank(this.identifier2, this.value2);
        this.totalScoreRank3 = new TotalScoreRank(this.identifier3, this.value3);
        this.totalScoreRank4 = new TotalScoreRank(this.identifier4, this.value4);
    }

    @After
    public void reset(){
        this.totalScoreRank1.resetThreshold();
    }

    @Test
    public void getRankTest(){
        assertEquals(1, this.totalScoreRank1.getRank());
        assertEquals(2, this.totalScoreRank2.getRank());
        assertEquals(3, this.totalScoreRank3.getRank());
        assertEquals(4, this.totalScoreRank4.getRank());
    }

    @Test
    public void getIdentifierTest(){
        assertEquals(this.identifier1, this.totalScoreRank1.getIdentifier());
        assertEquals(this.identifier2, this.totalScoreRank2.getIdentifier());
        assertNotEquals(this.identifier4, this.totalScoreRank3.getIdentifier());
        assertNotEquals(this.identifier3, this.totalScoreRank4.getIdentifier());
    }

    @Test
    public void getValueTest(){
        assertNotEquals(100, this.totalScoreRank1.getValue());
        assertEquals(10000, this.totalScoreRank2.getValue());
        assertEquals(100, this.totalScoreRank3.getValue());
        assertNotEquals(100000, this.totalScoreRank4.getValue());
    }

    @Test
    public void resetThresholdTest(){
        this.totalScoreRank1.resetThreshold();
        TotalScoreRank totalScoreRank5 = new TotalScoreRank("Player 5", 123);
        assertEquals(1, totalScoreRank5.getRank());
    }

}
