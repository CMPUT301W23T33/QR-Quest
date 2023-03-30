package com.example.qrquest;

import static org.junit.Assert.assertEquals;

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
    public void getQueryRankTest(){
    }

}
