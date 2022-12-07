package com.example.testinterface_two.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class MyShopCart extends MyShopCartId implements Serializable {
    public String UserId;
    public String MyShopCartImage;
    public String MyShopCartName;
    public String MyShopCartCount;
    public String MyShopCartPrice;
    public String SellerID;
    public String MyShopCartItemId;


    @PropertyName("MyShopCartItemId")
    public String getMyShopCartItemId() {return MyShopCartItemId;}
    @PropertyName("MyShopCartItemId")
    public void setMyShopCartItemId(String myShopCartItemId) {MyShopCartItemId = myShopCartItemId;}

    @PropertyName("SellerID")
    public String getSellerID() {return SellerID;}
    @PropertyName("SellerID")
    public void setSellerID(String sellerID) {SellerID = sellerID;}

    public MyShopCart(){}

    @PropertyName("UserId")
    public String getUserId() {
        return UserId;
    }
    @PropertyName("UserId")
    public void setUserId(String userId) {
        UserId = userId;
    }
    @PropertyName("MyShopCartImage")
    public String getMyShopCartImage() {
        return MyShopCartImage;
    }
    @PropertyName("MyShopCartImage")
    public void setMyShopCartImage(String myShopCartImage) {
        MyShopCartImage = myShopCartImage;
    }
    @PropertyName("MyShopCartName")
    public String getMyShopCartName() {
        return MyShopCartName;
    }
    @PropertyName("MyShopCartName")
    public void setMyShopCartName(String myShopCartName) {
        MyShopCartName = myShopCartName;
    }
    @PropertyName("MyShopCartCount")
    public String getMyShopCartCount() {
        return MyShopCartCount;
    }
    @PropertyName("MyShopCartCount")
    public void setMyShopCartCount(String myShopCartCount) {
        MyShopCartCount = myShopCartCount;
    }
    @PropertyName("MyShopCartPrice")
    public String getMyShopCartPrice() {
        return MyShopCartPrice;
    }
    @PropertyName("MyShopCartPrice")
    public void setMyShopCartPrice(String myShopCartPrice) {
        MyShopCartPrice = myShopCartPrice;
    }
}
