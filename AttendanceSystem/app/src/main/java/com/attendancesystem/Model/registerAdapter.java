package com.attendancesystem.Model;

import java.util.Date;

public class registerAdapter {
    String name;
    String mobile;
    String email;
    String password;
    String gender;
    String usertype;
    String totalpresent;

    public String getTotalpresent() {
        return totalpresent;
    }

    public void setTotalpresent(String totalpresent) {
        this.totalpresent = totalpresent;
    }

    public registerAdapter()
    {

    }

    public registerAdapter(String name, String mobile, String email, String password, String gender, String usertype, String totalpresent) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.usertype = usertype;
        this.totalpresent = totalpresent;
    }

    public registerAdapter(String name, String mobile, String email, String password, String gender, String usertype)
    {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.usertype = usertype;
    }
    public registerAdapter(String name,String mobile,String email,String password,String gender)
    {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.usertype = usertype;
    }
    public registerAdapter(String name,String mobile,String email)
    {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }
    public registerAdapter(String email,String name) {
        this.email = email;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getUsertype() {
        return usertype;
    }
}
