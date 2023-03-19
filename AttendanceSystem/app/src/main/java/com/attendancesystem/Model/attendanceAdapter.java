package com.attendancesystem.Model;

public class attendanceAdapter {
    String time;
    String email;
    String name;
    String date;
    public attendanceAdapter() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public attendanceAdapter(String email, String time, String name, String date) {
        this.time = time;
        this.email = email;
        this.name = name;
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}