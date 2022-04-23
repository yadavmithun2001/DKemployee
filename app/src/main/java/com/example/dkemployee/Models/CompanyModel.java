package com.example.dkemployee.Models;

public class CompanyModel {
    String name,address,landlinenumber,email;

    public CompanyModel(String name, String address, String landlinenumber, String email) {
        this.name = name;
        this.address = address;
        this.landlinenumber = landlinenumber;
        this.email = email;
    }
    public CompanyModel(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandlinenumber() {
        return landlinenumber;
    }

    public void setLandlinenumber(String landlinenumber) {
        this.landlinenumber = landlinenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
