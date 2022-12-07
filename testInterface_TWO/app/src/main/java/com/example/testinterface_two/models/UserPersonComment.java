package com.example.testinterface_two.models;

public class UserPersonComment extends UserPersonCommentID{
    public String personComment;
    public String personStar;
    public String personSellerID;

    public String getPersonComment() {
        return personComment;
    }

    public void setPersonComment(String personComment) {
        this.personComment = personComment;
    }

    public String getPersonStar() {
        return personStar;
    }

    public void setPersonStar(String personStar) {
        this.personStar = personStar;
    }

    public String getPersonSellerID() {
        return personSellerID;
    }

    public void setPersonSellerID(String personSellerID) {
        this.personSellerID = personSellerID;
    }
}
