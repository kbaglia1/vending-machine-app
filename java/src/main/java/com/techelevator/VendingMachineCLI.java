package com.techelevator;

import com.techelevator.view.Menu;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_SALES_REPORT = "HIDDEN";
	// **All hidden menus items must be at the end of the MAIN_MENU_OPTIONS array**
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_SALES_REPORT };

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT,
			PURCHASE_MENU_OPTION_FINISH_TRANSACTION };

	private Menu menu;
	File logFile = new File("src/main/resources/Log.txt");

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		VendingMachine vendingMachine = new VendingMachine(getVendingMachineInfo());
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				vendingMachine.displayItems();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				while (true) {
					System.out.println("Current money provided: $" + vendingMachine.getBalance());
					choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
					if (choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
						writeToFile(vendingMachine.deposit(getDepositAmount()));
					} else if (choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
						vendingMachine.displayItems();
						writeToFile(vendingMachine.selectProduct(getSlotId(vendingMachine)));
					} else if (choice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
						writeToFile(vendingMachine.returnChange());
						break;
					}
				}
			} else if (choice.equals(MAIN_MENU_OPTION_SALES_REPORT)){
				createSalesReport(vendingMachine.getSalesReport());
			} else break;
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}

	public List<Slot> getVendingMachineInfo() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Input the path to the vending machine configuration file: ");
		String path = scanner.nextLine();
		List<Slot> slots = new ArrayList<>();
		try (Scanner fileScanner = new Scanner(new File(path))) {
			while (fileScanner.hasNextLine()) {
				String[] slotProperties = fileScanner.nextLine().split("\\|");
				 try {
				 	slots.add(new Slot(slotProperties[0], slotProperties[1], slotProperties[2], slotProperties[3]));
				 } catch (ArrayIndexOutOfBoundsException e) {
					 System.out.println("Your vending machine configuration file has improper formatting.");
				 }
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found during vending machine creation, no setup performed.");
			System.out.println("Please restart and provide a valid setup file.");
		}
		return slots;
	}

	public BigDecimal getDepositAmount() {
		Scanner depositScanner = new Scanner(System.in);
		System.out.println("");
		System.out.print("Please insert bills: $1, $5, $10, $20 >>> $");
		String depositIn = depositScanner.nextLine();
		while (!depositIn.equals("1") && !depositIn.equals("5") && !depositIn.equals("10") && !depositIn.equals("20") ) {
			System.out.println("Invalid dollar amount. Please indicate 1, 5, 10, 20");
			depositIn = depositScanner.nextLine();
		}
		return new BigDecimal(depositIn).setScale(2, RoundingMode.HALF_UP);
	}

	public String getSlotId(VendingMachine vendingMachine) {
		Scanner identifier = new Scanner(System.in);
		System.out.println("Please make your slot selection: ");
		String slotIdentifier = identifier.nextLine();

		while ((vendingMachine.getSlotByIdentifier(slotIdentifier)==null)) {
			System.out.println("Select a valid slot, ex. A1");
			slotIdentifier = identifier.nextLine();
		}
		return slotIdentifier;
	}

	public void writeToFile(String line) {
		try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(this.logFile, true))) {
			printWriter.print(line);
		} catch (FileNotFoundException e) {
			System.out.println("Please create the Log.txt file before using the Vending Machine...");
		}
	}

	public void createSalesReport(String report) {
		String path = "src/main/resources/" + "Sales_Report_" +
				new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss_a").format(new java.util.Date()) + ".txt";

		File salesReport = new File(path);
		try {
			salesReport.createNewFile();
		} catch (IOException e) {
			System.out.println("Error creating a new Sales Report file...");
		};

		try (PrintWriter printWriter = new PrintWriter(salesReport)) {
		printWriter.println(report);
		}
		catch (Exception e){
			System.out.println("Error writing to the Sales Report file...");
		}
		System.out.println("Sales report created.");
	}

}
