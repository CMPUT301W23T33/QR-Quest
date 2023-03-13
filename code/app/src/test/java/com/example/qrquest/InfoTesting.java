package com.example.qrquest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

import java.util.Date;

/**
 * This test class tests the methods of the Info class
 * @author Abinand Nanthananthan
 */
public class InfoTesting {
    @Test
    /**
     * This function tests the getter and setter methods related to a Player's QRCode's comment(s)
     */
    public void testComment() {
        Info testInfo = new Info();
        assertNull(testInfo.getComment());
        testInfo.setComment("Comment 1");
        assertNotEquals("Comment 2",testInfo.getComment());
        assertSame("Comment 1",testInfo.getComment());
    }
    @Test
    /**
     * This function tests the getter and setter methods related to a Player's QRCode Region
     */
    public void testRegion() {
        Info testInfo = new Info();
        assertEquals(null,testInfo.getRegion());
        testInfo.setRegion("Canada");
        assertNotEquals("Spain",testInfo.getRegion());
        assertSame("Canada",testInfo.getRegion());
    }

    @Test
    /**
     * This function tests the getter and setter methods related to the username of the owner of a QRCode
     */
    public void testUsername() {
        Info testInfo = new Info();
        assertNull(testInfo.getUsername());
        testInfo.setUsername("Username 1");
        assertNotEquals("Username 2",testInfo.getUsername());
        assertSame("Username 1",testInfo.getUsername());
    }
    @Test
    /**
     * This function tests the getter and setter methods related to a Player's QRCode timestamp
     */
    public void testTimeStamp() {
        Info testInfo = new Info();
        Date testDate = new Date(2023,03,13);
        Date testDate2 = new Date(2023,03,12);
        assertNull(testInfo.getTimeStamp());
        testInfo.setTimeStamp(testDate);
        assertNotEquals(testDate2,testInfo.getTimeStamp());
        assertSame(testDate,testInfo.getTimeStamp());
    }

}
