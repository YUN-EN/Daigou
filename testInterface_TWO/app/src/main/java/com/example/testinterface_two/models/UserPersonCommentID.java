package com.example.testinterface_two.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class UserPersonCommentID {
    @Exclude
    public String UserPersonCommentID;
    public <T extends UserPersonCommentID> T withId (@NonNull final String id) {
        this.UserPersonCommentID = id;
        return (T) this;
    }
}
