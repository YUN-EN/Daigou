package com.example.testinterface_two.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class CoinRecordID {
    @Exclude
    public String CoinRecordID;
    public <T extends CoinRecordID> T withId (@NonNull final String id) {
        this.CoinRecordID = id;
        return (T) this;
    }
}
