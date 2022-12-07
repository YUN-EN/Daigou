package com.example.testinterface_two.models;

import androidx.annotation.NonNull;

public class CoinSpendID {
    public String CoinSpendID;
    public <T extends CoinSpendID> T withId (@NonNull final String id) {
        this.CoinSpendID = id;
        return (T) this;
    }
}
