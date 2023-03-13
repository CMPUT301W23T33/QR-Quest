package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class HashTest {

    @Test
    public void testGenerateQRCodeName() {
        String hexString1 = "aabbccdd";
        String hexString2 = "ddccbbaa";
        String hexString3 = "aabbccdd";
        String name1 = Utilities.hashName(hexString1);
        String name2 = Utilities.hashName(hexString2);
        String name3 = Utilities.hashName(hexString3);
        assertEquals(name3, name1);
        assertNotEquals(name2, name1);
    }

    @Test
    public void testGenerateQRScore() {
        String hexString1 = "aabbccdd";
        String hexString2 = "ddccbbaa";
        String hexString3 = "ffffffff";
        String hexString4 = "00000000";
        int name1 = Utilities.hashScore(hexString1);
        int name2 = Utilities.hashScore(hexString2);
        int name3 = Utilities.hashScore(hexString3);
        int name4 = Utilities.hashScore(hexString4);
        assertEquals(name1, name2);
        assertNotEquals(name2, name3);
        assertEquals(name4, 384);
        assertEquals(name3, 816);
    }
}
