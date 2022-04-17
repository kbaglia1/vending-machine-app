package com.techelevator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VendingMachine {
    private final int DEFAULT_INVENTORY = 5;
    private List<Slot> slotList = new ArrayList<>();
    private BigDecimal balance = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

    public VendingMachine(List<Slot> slotList) {
        this.slotList = slotList;
    }

    public List<Slot> getSlotList() {
        return slotList;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Slot slot : this.slotList) {
             output.append("Slot Identifier: ").append(slot.getSlotIdentifier()).append(" | Item Name: ").append(slot.getItemName()).append(" | Item Price: ").append(slot.getItemPrice()).append(" | Item Type: + ").append(slot.getItemType()).append(" | Inventory: ").append(slot.getItemInventory()).append("\n");
        }
        return output.toString();
    }

    public void displayItems() {
        for (Slot slot : this.slotList) {
            if (slot.getItemInventory() < 1) System.out.println(slot.getSlotIdentifier() + ": " + slot.getItemName() + " SOLD OUT");
            else System.out.println(slot.getSlotIdentifier() + ": " + slot.getItemName() + " $" + slot.getItemPrice() + ", QTY Available: " + slot.getItemInventory());
        }
    }

    public Slot getSlotByIdentifier(String identifier) {
        for (Slot slot : this.slotList) if (slot.getSlotIdentifier().equals(identifier)) return slot;
        return null;
    }

    public String deposit(BigDecimal depositAmount) {
        this.balance = this.balance.add(depositAmount);
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a").format(new java.util.Date()) + " FEED MONEY: $" + depositAmount  + " $" + this.balance + "\n";
    }

    public String selectProduct(String slotIdentifier){
        if (this.balance.equals(new BigDecimal(0))){
            System.out.println("You must deposit money before making a selection.");
        }
        Slot slot = getSlotByIdentifier(slotIdentifier);
        if (slot == null) return "";
        if (balance.compareTo(slot.getItemPrice()) < 0) {
            System.out.println("Please enter more money or pick a cheaper item");
            return "";
        }
        if (slot.getItemInventory() < 1) {
            System.out.println("Item is SOLD OUT, please choose another option");
            return "";
        }
        balance = this.balance.subtract(slot.getItemPrice());
        slot.setItemInventory(slot.getItemInventory()-1);
        if (slot.getItemType().equals("Chip")){
            System.out.println("Crunch Crunch, Yum!");
        } else if (slot.getItemType().equals("Candy")) {
            System.out.println("Munch Munch, Yum!");
        } else if (slot.getItemType().equals("Drink")) {
            System.out.println("Glug Glug, Yum!");
        } else {
            System.out.println("Chew Chew, Yum!");
        }
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a").format(new java.util.Date()) + " " + slot.getItemName() + " " + slot.getSlotIdentifier()  + " $" + slot.getItemPrice() + " $" + this.balance + "\n";
    }

    public String returnChange() {
        System.out.println("Thank you for your purchase(s)!");
        System.out.println("Please retrieve your change of $" + this.getBalance() + " below.");
        System.out.println("Your change should include: ");
        BigDecimal pennyConverter = new BigDecimal(100);
        int balanceInPennies = getBalance().multiply(pennyConverter).intValue();
        int quarters = (int) (balanceInPennies / 25);
        balanceInPennies -= (quarters * 25);
        int dimes = (int) (balanceInPennies / 10);
        balanceInPennies -= (dimes * 10);
        int nickels = (int) (balanceInPennies / 5);
        String logText = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a").format(new java.util.Date()) + " GIVE CHANGE: $" + this.balance + " $0.00\n";
        this.balance = new BigDecimal(0);
        System.out.printf("%d quarter(s), %d dime(s), and %d nickel(s).\n", quarters, dimes, nickels);
        System.out.println("Have a great day!");
        return logText;
    }

    public String getSalesReport() {
        StringBuilder reportBuilder = new StringBuilder();
        double runningTotal = 0;

        for (Slot slot : slotList){
            reportBuilder.append(slot.getItemName()).append("|").append(DEFAULT_INVENTORY - slot.getItemInventory()).append("\n");
            runningTotal += (5.00 - slot.getItemInventory()) * slot.getItemPrice().doubleValue();
        }
        reportBuilder.append("\n**TOTAL SALES** $").append(new BigDecimal(runningTotal).setScale(2, RoundingMode.HALF_UP));
        return reportBuilder.toString();
    }
}
