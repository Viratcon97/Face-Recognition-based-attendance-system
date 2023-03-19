package com.attendancesystem.Model;

public class showAttendance {

    String email;
    String name;

    String totalpresent;

    public showAttendance() {
    }

    public String getTotalpresent() {

        return totalpresent;
    }

    public void setTotalpresent(String totalpresent) {
        this.totalpresent = totalpresent;
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
