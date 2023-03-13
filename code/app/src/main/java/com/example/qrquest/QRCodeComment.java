package com.example.qrquest;

/**
 * This class defines the comments of QR Codes
 * Call ArrayList<Comment> to use this class
 * @author Dang Viet Anh Dinh
 */
public class QRCodeComment {

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
    public QRCodeComment(){}

    /**
     * This method defines a comment made by a player
     * @param username: the username of the one who made the comment
     * @param comment: the comment made by the player
     */
    public QRCodeComment(String username, String comment){
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
     * @param username: the username of the player
     */
    protected void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method sets the comment content made by the player
     * @param comment: the comment made by the player
     */
    protected void setComment(String comment) {
        this.comment = comment;
    }

}
