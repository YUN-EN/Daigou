package com.example.testinterface_two.models;

public class Notify {
    public String userId;
    public String notifyMessage;
    public String postId;
    public boolean isPost;
    public String posterID;

    public boolean isPost() {
        return isPost;
    }

    public String getPosterID() {
        return posterID;
    }

    public void setPosterID(String posterID) {
        this.posterID = posterID;
    }

    public Notify(String userId, String notifyMessage, String postId, boolean isPost , String posterID) {
        this.userId = userId;
        this.notifyMessage = notifyMessage;
        this.postId = postId;
        this.isPost = isPost;
        this.posterID = posterID;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public Notify(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(String notifyMessage) {
        this.notifyMessage = notifyMessage;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


}
