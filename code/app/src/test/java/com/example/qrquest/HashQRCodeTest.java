package com.example.qrquest;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashQRCodeTest {

    public HashQRCodeTest() {};

    @Test
    public void testGenerateQRCodeName() {
        String hexString1 = "aabbccdd";
        String hexString2 = "ddccbbaa";
        String hexString3 = "aabbccdd";
        String name1 = HashQRCodeExample.generateQRCodeName(hexString1);
        String name2 = HashQRCodeExample.generateQRCodeName(hexString2);
        String name3 = HashQRCodeExample.generateQRCodeName(hexString3);
        assertEquals(name3, name1);
        assertNotEquals(name2, name1);
    }
}
