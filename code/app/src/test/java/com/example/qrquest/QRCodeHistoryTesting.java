package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * This test class tests the QRCodeHistory class methods
 * @author Abinand Nanthananthan
 */
public class QRCodeHistoryTesting {
    @Test
    /**
     * This function tests the getter and setter methods related to a QR code within a QRCodeHistory
     */
    public void testQrCode() {
        QRCodeHistory testQRCodeHistory = new QRCodeHistory();
        assertNull(testQRCodeHistory.getQrCode());
        testQRCodeHistory.setQrCode("QRCode #1");
        assertNotEquals("QRCode #2",testQRCodeHistory.getQrCode());
        assertSame("QRCode #1",testQRCodeHistory.getQrCode());
    }

    @Test
    /**
     * This function tests the getter and setter methods related to a QRCode's (within a QRCodeHistory) score
     */
    public void testScore() {
        QRCodeHistory testQRCodeHistory = new QRCodeHistory();
        assertEquals(0,testQRCodeHistory.getScore());
        testQRCodeHistory.setScore(100);
        assertNotEquals(200,testQRCodeHistory.getScore());
        assertSame(100,testQRCodeHistory.getScore());
    }

}
