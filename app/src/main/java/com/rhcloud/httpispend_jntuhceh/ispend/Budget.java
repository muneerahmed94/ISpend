package com.rhcloud.httpispend_jntuhceh.ispend;

/**
 * Created by Muneer on 22-03-2016.
 */
public class Budget {
    String email, food, entertainment, electronics, fashion, other, total;

    public Budget(String email, String food,  String entertainment, String electronics, String fashion, String other, String total) {
        this.electronics = electronics;
        this.email = email;
        this.entertainment = entertainment;
        this.fashion = fashion;
        this.food = food;
        this.other = other;
        this.total = total;
    }
}
