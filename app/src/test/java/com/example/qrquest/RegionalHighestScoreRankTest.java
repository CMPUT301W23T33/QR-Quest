package com.example.qrquest;

import static org.junit.Assert.assertEquals;

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
    public void getQueryRankTest(){
    }

}
