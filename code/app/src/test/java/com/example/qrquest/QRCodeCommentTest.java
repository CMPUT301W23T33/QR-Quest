package com.example.qrquest;

import org.junit.Test;

import static org.junit.Assert.*;

public class QRCodeCommentTest {

    String username = "Dogwood";
    String commentContent = "Hello world!";

    QRCodeComment comment = new QRCodeComment();
    QRCodeComment comment1 = new QRCodeComment(username, commentContent);

    @Test
    public void getCommentTest() {
        assertNull(comment.getComment());
        assertEquals(comment1.getComment(), commentContent);
    }

    @Test
    public void getUsernameTest() {
        assertNull(comment.getUsername());
        assertEquals(comment1.getUsername(), username);
    }

}
