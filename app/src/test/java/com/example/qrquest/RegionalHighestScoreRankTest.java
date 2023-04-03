package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests class RegionalHighestScoreRank
 * @author Dang Viet Anh Dinh
 */
public class RegionalHighestScoreRankTest {

    private String identifier1, identifier2, identifier3, identifier4;
    private Integer value1, value2, value3, value4;
    private RegionalHighestScoreRank regionalHighestScoreRank1, regionalHighestScoreRank2,
            regionalHighestScoreRank3, regionalHighestScoreRank4;

    @Before
    public void setup() {
        this.identifier1 = "Player 1";
        this.identifier2 = "Player 2";
        this.identifier3 = "Player 3";
        this.identifier4 = "Player 4";
        this.value1 = 500;
        this.value2 = 50;
        this.value3 = 50;
        this.value4 = 5;
        this.regionalHighestScoreRank1 = new RegionalHighestScoreRank(this.identifier1, this.value1);
        this.regionalHighestScoreRank2 = new RegionalHighestScoreRank(this.identifier2, this.value2);
        this.regionalHighestScoreRank3 = new RegionalHighestScoreRank(this.identifier3, this.value3);
        this.regionalHighestScoreRank4 = new RegionalHighestScoreRank(this.identifier4, this.value4);
    }

    @After
    public void reset(){
        this.regionalHighestScoreRank1.resetThreshold();
    }

    @Test
    public void getRankTest(){
        assertEquals(1, this.regionalHighestScoreRank1.getRank());
        assertEquals(2, this.regionalHighestScoreRank2.getRank());
        assertEquals(3, this.regionalHighestScoreRank3.getRank());
        assertEquals(4, this.regionalHighestScoreRank4.getRank());
    }

    @Test
    public void getIdentifierTest(){
        assertEquals(this.identifier1, this.regionalHighestScoreRank1.getIdentifier());
        assertEquals(this.identifier2, this.regionalHighestScoreRank2.getIdentifier());
        assertNotEquals(this.identifier4, this.regionalHighestScoreRank3.getIdentifier());
        assertNotEquals(this.identifier3, this.regionalHighestScoreRank4.getIdentifier());
    }

    @Test
    public void getValueTest(){
        assertNotEquals(5, this.regionalHighestScoreRank1.getValue());
        assertEquals(50, this.regionalHighestScoreRank2.getValue());
        assertEquals(50, this.regionalHighestScoreRank3.getValue());
        assertNotEquals(500, this.regionalHighestScoreRank4.getValue());
    }

    @Test
    public void resetThresholdTest(){
        this.regionalHighestScoreRank1.resetThreshold();
        RegionalHighestScoreRank regionalHighestScoreRank5 = new RegionalHighestScoreRank("Player 5", 123);
        assertEquals(1, regionalHighestScoreRank5.getRank());
    }

}
