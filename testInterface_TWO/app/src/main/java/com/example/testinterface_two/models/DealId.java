package com.example.testinterface_two.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class DealId {
    @Exclude
    public String DealId;
    public <T extends DealId> T withId (@NonNull final String id) {
        this.DealId = id;
        return (T) this;
    }
}
