package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

/**
 * This class tests class ScannedNumberRank
 * @author Dang Viet Anh Dinh
 */
public class ScannedNumberRankTest {

    private String identifier1, identifier2, identifier3, identifier4;
    private Integer value1, value2, value3, value4;
    private ScannedNumberRank scannedNumberRank1, scannedNumberRank2,
            scannedNumberRank3, scannedNumberRank4;

    @Before
    public void setup() {
        this.identifier1 = "Player 1";
        this.identifier2 = "Player 2";
        this.identifier3 = "Player 3";
        this.identifier4 = "Player 4";
        this.value1 = 100;
        this.value2 = 10;
        this.value3 = 10;
        this.value4 = 1;
        this.scannedNumberRank1 = new ScannedNumberRank(this.identifier1, this.value1);
        this.scannedNumberRank2 = new ScannedNumberRank(this.identifier2, this.value2);
        this.scannedNumberRank3 = new ScannedNumberRank(this.identifier3, this.value3);
        this.scannedNumberRank4 = new ScannedNumberRank(this.identifier4, this.value4);
    }

    @After
    public void reset(){
        this.scannedNumberRank1.resetThreshold();
    }

    @Test
    public void getRankTest(){
        assertEquals(1, this.scannedNumberRank1.getRank());
        assertEquals(2, this.scannedNumberRank2.getRank());
        assertEquals(3, this.scannedNumberRank3.getRank());
        assertEquals(4, this.scannedNumberRank4.getRank());
    }

    @Test
    public void getIdentifierTest(){
        assertEquals(this.identifier1, this.scannedNumberRank1.getIdentifier());
        assertEquals(this.identifier2, this.scannedNumberRank2.getIdentifier());
        assertNotEquals(this.identifier4, this.scannedNumberRank3.getIdentifier());
        assertNotEquals(this.identifier3, this.scannedNumberRank4.getIdentifier());
    }

    @Test
    public void getValueTest(){
        assertNotEquals(1, this.scannedNumberRank1.getValue());
        assertEquals(10, this.scannedNumberRank2.getValue());
        assertEquals(10, this.scannedNumberRank3.getValue());
        assertNotEquals(100, this.scannedNumberRank4.getValue());
    }

    @Test
    public void resetThresholdTest(){
        this.scannedNumberRank1.resetThreshold();
        ScannedNumberRank scannedNumberRank5 = new ScannedNumberRank("Player 5", 123);
        assertEquals(1, scannedNumberRank5.getRank());
    }

}
