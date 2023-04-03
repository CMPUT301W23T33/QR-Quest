package com.example.qrquest;

/**
 * This class defines the comments of QR Codes
 * @author Dang Viet Anh Dinh
 */
public class Comment {

    /**
     * This member represents the username of the one who made the comment
     */
    private String username;

    /**
     * This member represents the comment made by the player
     */
    private String comment;

    /**
     * This method defines a default comment
     */
    public Comment(){}

    /**
     * This method defines a comment made by a player
     * @param username the username of the one who made the comment
     * @param comment the comment made by the player
     */
    public Comment(String username, String comment){
        setUsername(username);
        setComment(comment);
    }

    /**
     * This method retrieves the username of the one who made the comment
     * @return the username of the player
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * This method retrieves the comment made by the player
     * @return the comment made by the player
     */
    public String getComment(){
        return this.comment;
    }

    /**
     * This method sets the username for the one who made the comment
     * @param username the username of the player
     */
    private void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method sets the comment content made by the player
     * @param comment the comment made by the player
     */
    private void setComment(String comment) {
        this.comment = comment;
    }

}
