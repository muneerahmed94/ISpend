package com.rhcloud.httpispend_jntuhceh.ispend;

/**
 * Created by Muneer on 06-03-2016.
 */
public class Value {
    String category, budget, spends, remaining;

    public Value(String category, String budget, String spends, String remaining) {
        this.category = category;
        this.budget = budget;
        this.spends = spends;
        this.remaining = remaining;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getSpends() {
        return spends;
    }

    public void setSpends(String spends) {
        this.spends = spends;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }
}
