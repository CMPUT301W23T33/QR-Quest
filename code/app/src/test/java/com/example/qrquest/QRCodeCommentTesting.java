package com.example.qrquest;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class QRCodeCommentTesting {
    @Test
    /**
     * This function tests the getter and setter methods related to a QRCode's commenter username
     */
    public void testUsername() {
        QRCodeComment testQRCodeComment = new QRCodeComment();
        assertNull(testQRCodeComment.getUsername());
        testQRCodeComment.setUsername("Commenter 1");
        assertNotEquals("QRCode Owner",testQRCodeComment.getUsername());
        assertSame("Commenter 1",testQRCodeComment.getUsername());
    }
    @Test
    /**
     * This function tests the getter and setter methods related to a QRCode's commenter comment(s)
     */
    public void testComment() {
        QRCodeComment testQRCodeComment = new QRCodeComment();
        assertNull(testQRCodeComment.getComment());
        testQRCodeComment.setComment("Comment 1");
        assertNotEquals("QRCode Caption",testQRCodeComment.getComment());
        assertSame("Comment 1",testQRCodeComment.getComment());
    }
}
