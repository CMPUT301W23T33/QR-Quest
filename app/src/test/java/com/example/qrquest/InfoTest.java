package com.example.qrquest;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class tests class Info
 * @author Thea Nguyen
 */
public class InfoTest {
    Info info = new Info();

    @Test
    public void getSetCommentTest() {
        String comment = "Hello world!";
        assertNull(info.getComment());
        info.setComment(comment);
        assertNotNull(info.getComment());
        assertEquals(info.getComment(), comment);
    }

    @Test
    public void getSetRegionTest() {
        String region = "Alberta";
        assertNull(info.getRegion());
        info.setRegion(region);
        assertNotNull(info.getRegion());
        assertEquals(info.getRegion(), region);
    }

    @Test
    public void getSetUsernameTest() {
        String username = "Dogwood";
        assertNull(info.getUsername());
        info.setUsername(username);
        assertNotNull(info.getUsername());
        assertEquals(info.getUsername(), username);
    }

    @Test
    public void getSetTimeStamp() {
        Date date = new Date();
        assertNull(info.getTimeStamp());
        info.setTimeStamp(date);
        assertNotNull(info.getTimeStamp());
        assertEquals(info.getTimeStamp(), date);
    }

    @Test
    public void getImagePathTest() {
        // set up
        info.setUsername("Dogwood");
        info.setQrCode("QR Code #1");
        info.setTimeStamp(new Date());
        String time = new SimpleDateFormat("MM_dd_yyyy_hh_mm_ss", Locale.CANADA)
                .format(info.getTimeStamp());

        // test
        String imagePath = "Images/" + info.getUsername() + "_" + info.getQrCode() + "_" + time + ".jpg";
        assertEquals(imagePath, info.getImagePath());
    }
}
