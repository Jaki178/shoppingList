package com.example.ebauer16.shoppinglist;

/**
 * Created by ebauer16 on 12.03.2019.
 */

public class Article {
    //private int id;
    private String text;
    private String amount;

    public Article(String text, String amount) {
       // this.id = id;
        this.text = text;
        this.amount = amount;
    }

   /* public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    } */

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return text + ";" + amount;
    }
}
