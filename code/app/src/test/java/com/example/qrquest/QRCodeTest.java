package com.example.qrquest;

import org.junit.Test;

import static org.junit.Assert.*;

public class QRCodeTest {

    QRCode qrCode = new QRCode();

    @Test
    public void getSetQRCodeTest() {
        String qrCodeName = "QR Code #1";
        assertNull(qrCode.getQrCode());
        qrCode.setQrCode(qrCodeName);
        assertNotNull(qrCode.getQrCode());
        assertEquals(qrCodeName, qrCode.getQrCode());
    }

    @Test
    public void getSetHashedQRCodeTest() {
        String qrCodeName = "QR Code #2";
        assertNull(qrCode.getHashedQRCode());
        qrCode.setHashedQRCode(qrCodeName);
        assertNotNull(qrCode.getHashedQRCode());
        assertEquals(qrCodeName, qrCode.getHashedQRCode());
    }

    @Test
    public void getSetScoreTest() {
        int score = 2506;
        assertEquals(0, qrCode.getScore());
        qrCode.setScore(score);
        assertEquals(score, qrCode.getScore());
    }

    @Test
    public void getSetLatitudeTest() {
        double x = 25.06;
        assertEquals(0, qrCode.getLatitude(), 0);
        qrCode.setLatitude(x);
        assertEquals(x, qrCode.getLatitude(),0);
    }

    @Test
    public void getSetLongitudeTest() {
        double x = 20.03;
        assertEquals(0, qrCode.getLongitude(), 0);
        qrCode.setLongitude(x);
        assertEquals(x, qrCode.getLongitude(),0);
    }
}
