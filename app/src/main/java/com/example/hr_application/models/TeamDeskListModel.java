package com.example.hr_application.models;

public class TeamDeskListModel {
    private String teamName,key;

    public TeamDeskListModel() {
    }

    public TeamDeskListModel(String teamName, String key) {
        this.teamName = teamName;
        this.key = key;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
