package com.example.hr_application.models;

public class Feed {
    private String byName,date,information,position,uid,imageUrl,key;

    public Feed(String byName, String date, String information, String position,String uid,String imageUrl,String key) {
        this.byName = byName;
        this.date = date;
        this.information = information;
        this.position = position;
        this.key=key;
        this.imageUrl=imageUrl;
        this.uid=uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Feed() {
    }

    public String getByName() {
        return byName;
    }

    public void setByName(String byName) {
        this.byName = byName;
    }


    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
