package com.example.testinterface_two.models;

public class CoinRecord extends CoinRecordID{
    private boolean isVisible;
    private String userCoinRecord;
    private String dateCoinRecord;
    private String numCoinRecord;
    private String dealIdCoinRecord;
    private String categoryCoinRecord;

    public CoinRecord(String userCoinRecord, String dateCoinRecord, String numCoinRecord, String dealIdCoinRecord, String categoryCoinRecord) {
        this.userCoinRecord = userCoinRecord;
        this.dateCoinRecord = dateCoinRecord;
        this.numCoinRecord = numCoinRecord;
        this.dealIdCoinRecord = dealIdCoinRecord;
        this.categoryCoinRecord = categoryCoinRecord;
        this.isVisible = false;
    }

    public CoinRecord() {}

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getUserCoinRecord() {
        return userCoinRecord;
    }

    public void setUserCoinRecord(String userCoinRecord) {
        this.userCoinRecord = userCoinRecord;
    }

    public String getDateCoinRecord() {
        return dateCoinRecord;
    }

    public void setDateCoinRecord(String dateCoinRecord) {
        this.dateCoinRecord = dateCoinRecord;
    }

    public String getNumCoinRecord() {
        return numCoinRecord;
    }

    public void setNumCoinRecord(String numCoinRecord) {
        this.numCoinRecord = numCoinRecord;
    }

    public String getDealIdCoinRecord() {
        return dealIdCoinRecord;
    }

    public void setDealIdCoinRecord(String dealIdCoinRecord) {
        this.dealIdCoinRecord = dealIdCoinRecord;
    }

    public String getCategoryCoinRecord() {
        return categoryCoinRecord;
    }

    public void setCategoryCoinRecord(String categoryCoinRecord) {
        this.categoryCoinRecord = categoryCoinRecord;
    }
}
