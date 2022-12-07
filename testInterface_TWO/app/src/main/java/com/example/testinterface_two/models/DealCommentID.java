package com.example.testinterface_two.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class DealCommentID {
    @Exclude
    public String DealCommentID;
    public <T extends DealCommentID> T withId (@NonNull final String id) {
        this.DealCommentID = id;
        return (T) this;
    }
}
