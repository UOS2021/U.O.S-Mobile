package com.uof.uof_mobile.other;

public class Card {
    private String num;
    private String cvc;
    private String dueDate;

    public Card() {

    }

    public Card(String num, String cvc, String dueDate) {
        this.num = num;
        this.cvc = cvc;
        this.dueDate = dueDate;
    }

    public void clear() {
        num = "";
        cvc = "";
        dueDate = "";
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
