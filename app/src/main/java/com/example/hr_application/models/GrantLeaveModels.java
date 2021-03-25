package com.example.hr_application.models;

public class GrantLeaveModels {
    private String fromDate,toDate,name,uid,reason;

    public GrantLeaveModels() {
    }

    public GrantLeaveModels(String fromDate, String toDate, String name, String uid, String reason) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.name = name;
        this.uid = uid;
        this.reason = reason;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
