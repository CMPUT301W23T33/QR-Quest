package com.example.qrquest;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilitiesTest {

    // Hashing initialization
    String rawValue1 = "BFG5DGW54";
    String rawValue2 = "AFG5DGW54";

    @Test
    public void hashNameTest() {
        assertNotEquals(rawValue1, Utilities.hashName(rawValue1));
        assertNotEquals(Utilities.hashName(rawValue1), Utilities.hashName(rawValue2));
    }

    @Test
    public void hashScoreTest() {
        assertTrue(Utilities.hashScore(rawValue1) > 0);
        assertTrue(Utilities.hashScore(rawValue2) > 0);

        assertNotEquals(Utilities.hashScore(rawValue1), Utilities.hashScore(rawValue2));
    }

}