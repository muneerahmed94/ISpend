package com.rhcloud.httpispend_jntuhceh.ispend;

/**
 * Created by Muneer on 22-03-2016.
 */
public class Items {
    String itemName, itemCategory, itemPrice;

    public Items(String itemName, String itemCategory, String itemPrice) {
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
}
