package com.example.qrquest;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests class Utilities
 * @author Thea Nguyen
 */
public class UtilitiesTest {

    // Hashing initialization
    String rawValue1 = "BFG5DGW54";
    String rawValue2 = "AFG5DGW54";

    @Test
    public void hashScoreTest() {
        assertTrue(Utilities.hashScore(rawValue1) > 0);
        assertTrue(Utilities.hashScore(rawValue2) > 0);

        assertNotEquals(Utilities.hashScore(rawValue1), Utilities.hashScore(rawValue2));
    }

}