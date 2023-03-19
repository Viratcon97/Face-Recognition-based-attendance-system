package com.attendancesystem.Model;

public class attendancerequestreport {
    String email;
    String date;
    String status;

    public attendancerequestreport() {
    }

    public attendancerequestreport(String date, String email, String status) {
        this.date = date;
        this.email = email;
        this.status = status;
    }

    public attendancerequestreport(String date, String status) {
        this.date = date;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
