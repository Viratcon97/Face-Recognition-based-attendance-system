package com.attendancesystem.Model;

import android.content.Context;

public class changeAttendance {

    String email;
    String date;
    String time;
    String reason;
    String status;


    public changeAttendance() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public changeAttendance(String email, String date, String time, String reason, String status) {
        this.email = email;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = status;
    }

    public changeAttendance(String email, String date) {
        this.email = email;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
