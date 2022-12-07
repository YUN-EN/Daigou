package com.example.testinterface_two.models;

import com.google.firebase.firestore.FieldValue;

import java.util.Date;

public class SavePost {
    private String saveUserId;
    private String PostId;
    private String PostImage;
    private String PostTitle;
    private Date DateTime;

    public String getSaveUserId() {
        return saveUserId;
    }

    public void setSaveUserId(String saveUserId) {
        this.saveUserId = saveUserId;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getPostTitle() {
        return PostTitle;
    }

    public void setPostTitle(String postTitle) {
        PostTitle = postTitle;
    }

    public Date getDateTime() {
        return DateTime;
    }

    public void setDateTime(Date dateTime) {
        DateTime = dateTime;
    }
}
