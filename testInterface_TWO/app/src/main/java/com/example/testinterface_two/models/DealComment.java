package com.example.testinterface_two.models;

public class DealComment extends DealCommentID{
    private String user;
    private String commentText;
    private String star;
    private String commentImage;
    private String commentDate;
    private String commentItemID;
    private String commentItemName;
    private boolean isVisible;

    public DealComment() {
    }

    public DealComment(String user, String commentText, String star, String commentImage, String commentDate, String commentItemID, String commentItemName) {
        this.user = user;
        this.commentText = commentText;
        this.star = star;
        this.commentImage = commentImage;
        this.commentDate = commentDate;
        this.commentItemID = commentItemID;
        this.commentItemName = commentItemName;
        this.isVisible = false;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getCommentItemName() {
        return commentItemName;
    }

    public void setCommentItemName(String commentItemName) {
        this.commentItemName = commentItemName;
    }

    public String getCommentItemID() {
        return commentItemID;
    }

    public void setCommentItemID(String commentItemID) {
        this.commentItemID = commentItemID;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getCommentImage() {
        return commentImage;
    }

    public void setCommentImage(String commentImage) {
        this.commentImage = commentImage;
    }
}
