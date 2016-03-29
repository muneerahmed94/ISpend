package com.rhcloud.httpispend_jntuhceh.ispend;

/**
 * Created by Muneer on 10-03-2016.
 */
public class Purchase {
    String buyer, itemName, itemPrice, itemCategory;

    public Purchase(String buyer, String itemName, String itemPrice, String itemCategory) {
        this.buyer = buyer;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
    }
}
