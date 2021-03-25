package com.example.hr_application.models;

public class employeesModel {
    private String ImageUrl, username, Number, Developer, userid;

    public employeesModel() {
    }

    public employeesModel(String imageUrl, String username, String number, String developer, String userid) {
        this.ImageUrl = imageUrl;
        this.username = username;
        this.Number = number;
        this.Developer = developer;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public employeesModel(String imageUrl, String username, String number, String developer) {
        this.ImageUrl = imageUrl;
        this.username = username;
        this.Number = number;
        this.Developer = developer;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        this.Number = number;
    }

    public String getDeveloper() {
        return Developer;
    }

    public void setDeveloper(String developer) {
        this.Developer = developer;
    }
}
