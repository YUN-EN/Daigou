package com.example.testinterface_two.models;

import java.io.Serializable;
import java.util.Date;

public class Shop extends  ItemID implements Serializable {
    public String userID;
    public String itemImage;
    public String itemThing;
    public String itemCount;
    public String itemPrice;
    public Date itemDate;
    public String itemCategory;
    public String itemMind;


    public String getItemMind() {
        return itemMind;
    }
    public void setItemMind(String itemMind) {
        this.itemMind = itemMind;
    }
    public String getItemCategory() {
        return itemCategory;
    }
    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getItemImage() {
        return itemImage;
    }
    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
    public String getItemThing() {
        return itemThing;
    }
    public void setItemThing(String itemThing) {
        this.itemThing = itemThing;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Date getItemDate() {
        return itemDate;
    }

    public void setItemDate(Date itemDate) {
        this.itemDate = itemDate;
    }

    public Shop() {}

}
