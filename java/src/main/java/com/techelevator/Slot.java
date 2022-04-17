package com.techelevator;

import java.math.BigDecimal;

public class Slot {
    private String slotIdentifier;
    private String itemName;
    private BigDecimal itemPrice;
    private String itemType;
    private int itemInventory;

    public Slot(String slotIdentifier, String itemName, String itemPrice, String itemType) {
        this.slotIdentifier = slotIdentifier;
        this.itemName = itemName;
        this.itemPrice = new BigDecimal(itemPrice);
        this.itemType = itemType;
        this.itemInventory = 5;
    }

    public String getSlotIdentifier() {
        return slotIdentifier;
    }
    public String getItemName() {
        return itemName;
    }
    public BigDecimal getItemPrice() {
        return itemPrice;
    }
    public String getItemType() {
        return itemType;
    }
    public int getItemInventory() {
        return itemInventory;
    }
    public void setItemInventory(int itemInventory) {
        this.itemInventory = itemInventory;
    }


}
