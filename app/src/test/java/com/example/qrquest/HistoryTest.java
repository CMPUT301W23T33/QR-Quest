package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * This class tests class History
 * @author Dang Viet Anh Dinh
 */
public class HistoryTest {

    private String qrCode;
    private Integer qrScore;
    private History qrHistory1, qrHistory2;

    @Before
    public void setup() {
        this.qrCode = "QR Code";
        this.qrScore = 1000;
        this.qrHistory1 = new History();
        this.qrHistory2 = new History(this.qrCode, this.qrScore);
    }

    @Test
    public void getQrCodeTest(){
        assertNull(this.qrHistory1.getQrCode());
        assertEquals(this.qrHistory2.getQrCode(), this.qrCode);
    }

    @Test
    public void getScoreTest(){
        assertEquals(0, this.qrHistory1.getScore());
        assertEquals(1000, this.qrHistory2.getScore());

    }

}
