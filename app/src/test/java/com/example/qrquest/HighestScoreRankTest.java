package com.example.qrquest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests class HighestScoreRank
 * @author Dang Viet Anh Dinh
 */
public class HighestScoreRankTest {

    private String identifier1, identifier2, identifier3, identifier4;
    private Integer value1, value2, value3, value4;
    private HighestScoreRank highestScoreRank1, highestScoreRank2, highestScoreRank3, highestScoreRank4;

    @Before
    public void setup() {
        this.identifier1 = "Player 1";
        this.identifier2 = "Player 2";
        this.identifier3 = "Player 3";
        this.identifier4 = "Player 4";
        this.value1 = 1000;
        this.value2 = 100;
        this.value3 = 100;
        this.value4 = 10;
        this.highestScoreRank1 = new HighestScoreRank(this.identifier1, this.value1);
        this.highestScoreRank2 = new HighestScoreRank(this.identifier2, this.value2);
        this.highestScoreRank3 = new HighestScoreRank(this.identifier3, this.value3);
        this.highestScoreRank4 = new HighestScoreRank(this.identifier4, this.value4);
    }

    @After
    public void reset(){
        this.highestScoreRank1.resetThreshold();
    }

    @Test
    public void getQueryRankTest(){
        assertEquals(0, HighestScoreRank.getQueryRank(10000));
        assertEquals(1, HighestScoreRank.getQueryRank(1000));
        assertEquals(2, HighestScoreRank.getQueryRank(100));
        assertEquals(4, HighestScoreRank.getQueryRank(10));
    }

}
