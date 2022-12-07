package com.example.testinterface_two.models;


import java.io.Serializable;

    public class User  implements Serializable {
    public String userName ,userImage ,userPhone ,userPassword ,userMail ,Token;
    public String id;

    public String getUserMail() {
        return userMail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public User() {

    }
    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
