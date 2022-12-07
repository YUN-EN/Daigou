package com.example.testinterface_two.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class ItemID {
    @Exclude
    public String ItemID;
    public <T extends ItemID> T withId (@NonNull final String id) {
        this.ItemID = id;
        return (T) this;
    }
}
