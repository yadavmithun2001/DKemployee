package com.example.dkemployee.Models;

public class TrainingModel {
    String training_name,training_date,status;

    public TrainingModel(String training_name, String training_date,String status) {
        this.training_name = training_name;
        this.training_date = training_date;
        this.status = status;
    }
    public TrainingModel(){
    }

    public String getTraining_name() {
        return training_name;
    }

    public void setTraining_name(String training_name) {
        this.training_name = training_name;
    }

    public String getTraining_date() {
        return training_date;
    }

    public void setTraining_date(String training_date) {
        this.training_date = training_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
