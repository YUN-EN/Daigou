package com.example.testinterface_two.models;

public class CoinSpend extends CoinSpendID{
    private String userCoinSpend;
    private String dateCoinSpend;
    private String numCoinSpend;
    private String categoryCoinSpend;

    public String getUserCoinSpend() {
        return userCoinSpend;
    }

    public void setUserCoinSpend(String userCoinSpend) {
        this.userCoinSpend = userCoinSpend;
    }

    public String getDateCoinSpend() {
        return dateCoinSpend;
    }

    public void setDateCoinSpend(String dateCoinSpend) {
        this.dateCoinSpend = dateCoinSpend;
    }

    public String getNumCoinSpend() {
        return numCoinSpend;
    }

    public void setNumCoinSpend(String numCoinSpend) {
        this.numCoinSpend = numCoinSpend;
    }

    public String getCategoryCoinSpend() {
        return categoryCoinSpend;
    }

    public void setCategoryCoinSpend(String categoryCoinSpend) {
        this.categoryCoinSpend = categoryCoinSpend;
    }
}
