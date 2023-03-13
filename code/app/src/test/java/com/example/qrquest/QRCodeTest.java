package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * This class tests the methods of the QRCode class
 * @author Abinand Nanthananthan
 */
public class QRCodeTest {
    @Test
    /**
     * This function tests the getter and setter methods related to a QRCode's actual QR Code
     */
    public void testQrCode() {
        QRCode testQRCode = new QRCode();
        assertNull(testQRCode.getQrCode());
        testQRCode.setQrCode("abcdef90");
        assertNotEquals("9dddddfff",testQRCode.getQrCode());
        assertSame("abcdef90",testQRCode.getQrCode());
    }

    @Test
    /**
     * This function tests the getter and setter methods related to a QRCode's hashed QR Code
     */
    public void testHashedQRCode() {
        QRCode testQRCode = new QRCode();
        assertNull(testQRCode.getHashedQRCode());
        testQRCode.setHashedQRCode("HumanReadableName");
        assertNotEquals("nOnHuMaNReADablE",testQRCode.getHashedQRCode());
        assertSame("HumanReadableName",testQRCode.getHashedQRCode());
    }
    @Test
    /**
     * This function tests the getter and setter methods related to a QRCode's score
     */
    public void testScore() {
        QRCode testQRCode = new QRCode();
        assertEquals(0,testQRCode.getScore());
        testQRCode.setScore(10);
        assertNotEquals(11,testQRCode.getScore());
        assertSame(10,testQRCode.getScore());
    }
    @Test
    /**
     * This function tests the getter and setter methods related to a QRCode's location latitude
     */
    public void testLatitude() {
        QRCode testQRCode = new QRCode();
        assertEquals(0.0,testQRCode.getLatitude(),1);
        testQRCode.setLatitude(45.12);
        assertNotEquals(23.52,testQRCode.getLatitude());
        assertEquals(45.22,testQRCode.getLatitude(),0.2);
    }
    @Test
    /**
     * This function tests the getter and setter methods related to a QRCode's location longitude
     */
    public void testLongitude() {
        QRCode testQRCode = new QRCode();
        assertEquals(0.0,testQRCode.getLongitude(),1);
        testQRCode.setLongitude(-23.22);
        assertNotEquals(-12.34,testQRCode.getLongitude());
        assertEquals(-23.30,testQRCode.getLongitude(),0.2);
    }
}
