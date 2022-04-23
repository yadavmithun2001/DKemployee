package com.example.dkemployee.Models;

public class HolidayModel {
    String date,holiday;

    public HolidayModel(String date, String holiday) {
        this.date = date;
        this.holiday = holiday;
    }
    public HolidayModel(){
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }
}
