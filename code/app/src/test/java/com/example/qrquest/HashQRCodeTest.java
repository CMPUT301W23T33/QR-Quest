package com.example.qrquest;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashQRCodeTest {

    private HashQRCode hashQRCode;

    @Test
    public void testGenerateQRCodeName() {
        String hexString = "aabbccdd";
        assertEquals(".", HashQRCode.generateQRCodeName(hexString));
    }
}
