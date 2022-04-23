package com.example.dkemployee.Models;

public class UserModel {
    String uid,name,email,mobile,pf_url;

    public UserModel(String uid,String name, String email, String mobile,String pf_url) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.pf_url = pf_url;
    }
    public UserModel(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPf_url() {
        return pf_url;
    }

    public void setPf_url(String pf_url) {
        this.pf_url = pf_url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
