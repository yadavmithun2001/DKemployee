package com.example.dkemployee.Models;

public class SalaryModel {
    String month,amount,taxcode;

    public SalaryModel(){
    }
    public SalaryModel(String month, String amount, String tax_code) {
        this.month = month;
        this.amount = amount;
        this.taxcode = tax_code;
    }


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }
}
