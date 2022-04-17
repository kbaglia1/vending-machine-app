package com.techelevator;

import com.techelevator.VendingMachine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachineTests {

    private VendingMachine vendingMachine = new VendingMachine(getVendingMachineInfo());

    @Test
    public void test_deposit_no_deposit() {
        Assert.assertEquals(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP), vendingMachine.getBalance());
    }

    @Test
    public void test_deposit_zero_deposit() {
        vendingMachine.deposit(new BigDecimal(0));
        Assert.assertEquals(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP), vendingMachine.getBalance());
    }

    @Test
    public void test_deposit_nonzero_deposit() {
        vendingMachine.deposit(new BigDecimal(5));
        Assert.assertEquals(new BigDecimal(5).setScale(2, RoundingMode.HALF_UP), vendingMachine.getBalance());
    }

    @Test
    public void test_deposit_multiple_nonzero_deposit() {
        vendingMachine.deposit(new BigDecimal(5));
        vendingMachine.deposit(new BigDecimal(10));
        Assert.assertEquals(new BigDecimal(15).setScale(2, RoundingMode.HALF_UP), vendingMachine.getBalance());
    }

    @Test
    public void test_toString() {
        Assert.assertEquals("Slot Identifier: A1 | Item Name: Potato Crisps | Item Price: 3.05 | Item Type: + Chip | Inventory: 5\n" +
                "Slot Identifier: A2 | Item Name: Stackers | Item Price: 1.45 | Item Type: + Chip | Inventory: 5\n" +
                "Slot Identifier: A3 | Item Name: Grain Waves | Item Price: 2.75 | Item Type: + Chip | Inventory: 5\n" +
                "Slot Identifier: A4 | Item Name: Cloud Popcorn | Item Price: 3.65 | Item Type: + Chip | Inventory: 5\n" +
                "Slot Identifier: B1 | Item Name: Moonpie | Item Price: 1.80 | Item Type: + Candy | Inventory: 5\n" +
                "Slot Identifier: B2 | Item Name: Cowtales | Item Price: 1.50 | Item Type: + Candy | Inventory: 5\n" +
                "Slot Identifier: B3 | Item Name: Wonka Bar | Item Price: 1.50 | Item Type: + Candy | Inventory: 5\n" +
                "Slot Identifier: B4 | Item Name: Crunchie | Item Price: 1.75 | Item Type: + Candy | Inventory: 5\n" +
                "Slot Identifier: C1 | Item Name: Cola | Item Price: 1.25 | Item Type: + Drink | Inventory: 5\n" +
                "Slot Identifier: C2 | Item Name: Dr. Salt | Item Price: 1.50 | Item Type: + Drink | Inventory: 5\n" +
                "Slot Identifier: C3 | Item Name: Mountain Melter | Item Price: 1.50 | Item Type: + Drink | Inventory: 5\n" +
                "Slot Identifier: C4 | Item Name: Heavy | Item Price: 1.50 | Item Type: + Drink | Inventory: 5\n" +
                "Slot Identifier: D1 | Item Name: U-Chews | Item Price: 0.85 | Item Type: + Gum | Inventory: 5\n" +
                "Slot Identifier: D2 | Item Name: Little League Chew | Item Price: 0.95 | Item Type: + Gum | Inventory: 5\n" +
                "Slot Identifier: D3 | Item Name: Chiclets | Item Price: 0.75 | Item Type: + Gum | Inventory: 5\n" +
                "Slot Identifier: D4 | Item Name: Triplemint | Item Price: 0.75 | Item Type: + Gum | Inventory: 5\n", vendingMachine.toString());
    }

    @Test
    public void test_getSlotByIdentifier() {
        Assert.assertEquals("Cola", vendingMachine.getSlotByIdentifier("C1").getItemName());
        Assert.assertEquals(new BigDecimal("1.25"), vendingMachine.getSlotByIdentifier("C1").getItemPrice());
        Assert.assertEquals(5, vendingMachine.getSlotByIdentifier("C1").getItemInventory());
        Assert.assertEquals("Drink", vendingMachine.getSlotByIdentifier("C1").getItemType());
        Assert.assertEquals("C1", vendingMachine.getSlotByIdentifier("C1").getSlotIdentifier());
    }

    @Test
    public void test_selectProduct() {
        vendingMachine.deposit(new BigDecimal(10));
        vendingMachine.selectProduct("C1");
        Assert.assertEquals(4, vendingMachine.getSlotByIdentifier("C1").getItemInventory());
        Assert.assertEquals(new BigDecimal("8.75"), vendingMachine.getBalance());
    }

    @Test
    public void test_selectProduct_invalid_entry() {
        vendingMachine.deposit(new BigDecimal(10));
        vendingMachine.selectProduct("C55");
        for (Slot slot : vendingMachine.getSlotList()) {
            Assert.assertEquals(5, slot.getItemInventory());
        }
        Assert.assertEquals(new BigDecimal(10).setScale(2,RoundingMode.HALF_UP), vendingMachine.getBalance());
    }

    @Test
    public void test_returnChange_had_change() {
        vendingMachine.deposit(new BigDecimal(10));
        vendingMachine.returnChange();
        Assert.assertEquals(new BigDecimal(0), vendingMachine.getBalance());
    }

    @Test
    public void test_returnChange_had_no_change() {
        vendingMachine.returnChange();
        Assert.assertEquals(new BigDecimal(0), vendingMachine.getBalance());
    }

    @Test
    public void test_getSalesReport() {
        vendingMachine.deposit(new BigDecimal(20));
        vendingMachine.selectProduct("C1");
        Assert.assertEquals("Potato Crisps|0\n" +
                "Stackers|0\n" +
                "Grain Waves|0\n" +
                "Cloud Popcorn|0\n" +
                "Moonpie|0\n" +
                "Cowtales|0\n" +
                "Wonka Bar|0\n" +
                "Crunchie|0\n" +
                "Cola|1\n" +
                "Dr. Salt|0\n" +
                "Mountain Melter|0\n" +
                "Heavy|0\n" +
                "U-Chews|0\n" +
                "Little League Chew|0\n" +
                "Chiclets|0\n" +
                "Triplemint|0\n" +
                "\n" +
                "**TOTAL SALES** $1.25", vendingMachine.getSalesReport());
    }

    public List<Slot> getVendingMachineInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the path to the vending machine configuration file: ");
//		String path = scanner.nextLine();
        String path = "vendingmachine.csv";
        List<Slot> slots = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(path))) {
            while (fileScanner.hasNextLine()) {
                String[] slotProperties = fileScanner.nextLine().split("\\|");
                slots.add(new Slot(slotProperties[0], slotProperties[1], slotProperties[2], slotProperties[3]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found during vending machine creation, no setup performed.");
            System.out.println("Please restart and provide a valid setup file.");
        }
        return slots;
    }

}