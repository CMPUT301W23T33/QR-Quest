package com.example.qrquest;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    String username = "Dogwood";
    String commentContent = "Hello world!";

    Comment comment = new Comment();
    Comment comment1 = new Comment(username, commentContent);

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
