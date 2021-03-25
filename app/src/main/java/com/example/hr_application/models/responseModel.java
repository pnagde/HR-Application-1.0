package com.example.hr_application.models;

public class responseModel {
    private String name, fromDate,toDate,reason,leaveResponse,uid,time;

    public responseModel() {
    }

    public responseModel(String name, String fromDate, String toDate, String reason, String leaveResponse, String uid, String time) {
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.leaveResponse = leaveResponse;
        this.uid = uid;
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeaveResponse() {
        return leaveResponse;
    }

    public void setLeaveResponse(String leaveResponse) {
        this.leaveResponse = leaveResponse;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
