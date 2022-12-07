package com.example.testinterface_two.models;

import java.io.Serializable;

public class Deal extends DealId implements Serializable {
    public String dealUserId;
    public String dealItemName;
    public String dealItemImage;
    public String dealItemCount;
    public String dealItemPrice;
    public String sellerId;
    public String sellerName;
    public String dealDate;
    public String dealItemID;
    public boolean isReceive;
    public String dealItemFormat;
    public String dealFinishDate;
    public String dealCartID;
    public String dealBlockAddress;

    public String getDealBlockAddress() {
        return dealBlockAddress;
    }

    public void setDealBlockAddress(String dealBlockAddress) {
        this.dealBlockAddress = dealBlockAddress;
    }

    public String getDealCartID() {
        return dealCartID;
    }

    public void setDealCartID(String dealCartID) {
        this.dealCartID = dealCartID;
    }

    public String getDealFinishDate() {
        return dealFinishDate;
    }

    public void setDealFinishDate(String dealFinishDate) {
        this.dealFinishDate = dealFinishDate;
    }

    public String getDealItemFormat() {
        return dealItemFormat;
    }

    public void setDealItemFormat(String dealItemFormat) {
        this.dealItemFormat = dealItemFormat;
    }

    public boolean isReceive() {
        return isReceive;
    }

    public String getDealItemID() {
        return dealItemID;
    }

    public void setDealItemID(String dealItemID) {
        this.dealItemID = dealItemID;
    }

    public void setReceive(boolean receive) {
        isReceive = receive;
    }

    public String getDealUserId() {
        return dealUserId;
    }

    public void setDealUserId(String dealUserId) {
        this.dealUserId = dealUserId;
    }

    public String getDealItemName() {
        return dealItemName;
    }

    public void setDealItemName(String dealItemName) {
        this.dealItemName = dealItemName;
    }

    public String getDealItemImage() {
        return dealItemImage;
    }

    public void setDealItemImage(String dealItemImage) {
        this.dealItemImage = dealItemImage;
    }

    public String getDealItemCount() {
        return dealItemCount;
    }

    public void setDealItemCount(String dealItemCount) {
        this.dealItemCount = dealItemCount;
    }

    public String getDealItemPrice() {
        return dealItemPrice;
    }

    public void setDealItemPrice(String dealItemPrice) {
        this.dealItemPrice = dealItemPrice;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getDealAddress() {
        return dealAddress;
    }

    public void setDealAddress(String dealAddress) {
        this.dealAddress = dealAddress;
    }

    public String dealAddress;
    public Deal(){}

}
