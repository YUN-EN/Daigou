package com.example.testinterface_two.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class MyShopCartId {
    @Exclude
    public String MyShopCartId;
    public <T extends MyShopCartId> T withId (@NonNull final String id) {
        this.MyShopCartId = id;
        return (T) this;
    }
}
