package com.attendancesystem.Model;

public class InitialregisterAdapter {
    String name;
    String mobile;
    String email;
    String password;
    String gender;
    String usertype;

    public InitialregisterAdapter()
    {

    }
  
    public InitialregisterAdapter(String email, String name) {
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
