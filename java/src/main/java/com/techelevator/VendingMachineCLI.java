package com.techelevator;
/**************************************************************************************************************************
 *  This is your Vending Machine Command Line Interface (CLI) class
 *
 *  THIS IS NOT THE VENDING MACHINE!
 *
 *  It is the main process for the Vending Machine
 *
 *  THIS is where most, if not all, of your Vending Machine interactions should be coded
 *
 *  It is instantiated and invoked from the VendingMachineApp (main() application)
 *
 *  Your code vending machine related code should be placed in here
 ***************************************************************************************************************************/
import com.techelevator.view.Menu;         // Gain access to Menu class provided for the Capstone

import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;


public class VendingMachineCLI {

	// Read the file
	File inputFile = new File("./vendingmachine.csv");
	Scanner theDataInTheFile = new Scanner(inputFile);

	// Main menu options defined as constants
	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_REPORT = "Sales Report";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS,
			MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT,
			MAIN_MENU_OPTION_REPORT
	};

	private Menu vendingMenu;              // Menu object to be used by an instance of this class

	// Instantiating a vending machine
	VendingMachine vendingMachine = new VendingMachine();

	public VendingMachineCLI(Menu menu) throws FileNotFoundException {  // Constructor - user will pass a menu for this class to use
		this.vendingMenu = menu;           // Make the Menu the user object passed, our Menu
	}

	/**************************************************************************************************************************
	 *  VendingMachineCLI main processing loop
	 *
	 *  Display the main menu and process option chosen
	 *
	 *  It is invoked from the VendingMachineApp program
	 *
	 *  THIS is where most, if not all, of your Vending Machine objects and interactions
	 *  should be coded
	 *
	 *  Methods should be defined following run() method and invoked from it
	 *
	 ***************************************************************************************************************************/

	public void run() throws IOException {

		/***************************
		 * 	INSTANTIATE VM
		 ***************************/

		// Instantiate the vending machine
//		VendingMachine vendingMachine = new VendingMachine();

		// Add items to vending machine
		while (theDataInTheFile.hasNext()){
			String aLineFromFile = theDataInTheFile.nextLine();
			String[] components = aLineFromFile.split("\\|");
			// Instantiate a slot
			String slotID = components[0];
			String slotName = components[1];
			double slotPrice = Double.parseDouble(components[2]);
			String slotType = components[3];
			Slot aSlot = new Slot(slotID, slotName, slotPrice, slotType);
			// Stock items to slot
			if (slotType.equals("Chip")){
				Chips chip = new Chips(slotType, slotName);
				aSlot.stockSlot(chip);
			} else if (slotType.equals("Candy")){
				Candy candy = new Candy(slotType, slotName);
				aSlot.stockSlot(candy);
			} else if (slotType.equals("Drink")){
				Beverages drink = new Beverages(slotType, slotName);
				aSlot.stockSlot(drink);
			} else {
				Gum gum = new Gum(slotType, slotName);
				aSlot.stockSlot(gum);
			}
			// Add a slot to vending machine
			vendingMachine.addSlot(aSlot);
		}

		// Close input file
		theDataInTheFile.close();

		/***************************
		 * 	MAIN OPERATIONS
		 ***************************/

		boolean shouldProcess = true;         // Loop control variable

		while (shouldProcess) {                // Loop until user indicates they want to exit

			String choice = (String) vendingMenu.getChoiceFromOptions(MAIN_MENU_OPTIONS);  // Display menu and get choice

			switch (choice) {                  // Process based on user menu choice

				case MAIN_MENU_OPTION_DISPLAY_ITEMS:
					displayItems();           // invoke method to display items in Vending Machine
					break;                    // Exit switch statement

				case MAIN_MENU_OPTION_PURCHASE:
					purchaseItems();          // invoke method to purchase items from Vending Machine
					break;                    // Exit switch statement

				case MAIN_MENU_OPTION_EXIT:
					endMethodProcessing();    // Invoke method to perform end of method processing
					shouldProcess = false;    // Set variable to end loop
					break;                    // Exit switch statement

				case MAIN_MENU_OPTION_REPORT:
					createSalesReport();		// create sales report method
					System.out.println("\nSales report created.");
					System.out.println("Please navigate to SalesReport text file for revision.");
					break;
			}
		}
		return;                               // End method and return to caller
	}

	/********************************************************************************************************
	 * Methods used to perform processing
	 ********************************************************************************************************/

	public void displayItems() {      // static attribute used as method is not associated with specific object instance
		vendingMachine.displayItems();
	}

	public void purchaseItems() throws IOException {     // static attribute used as method is not associated with specific object instance
		Menu purchaseMenu = new Menu(System.in, System.out);                // Instantiate a menu for Vending Machine CLI main program to use
		VendingMachine purchaseCLI = new VendingMachine(purchaseMenu);
		purchaseCLI.purchaseItems(vendingMachine);
	}

	public void endMethodProcessing() { // static attribute used as method is not associated with specific object instance
		// Any processing that needs to be done before method ends
	}

	public void createSalesReport() throws IOException {
		// Creating file
		File outputFile = new File("SalesReport.txt");
		FileWriter aFileWriter = new FileWriter(outputFile, false);
		BufferedWriter aBufferedWriter = new BufferedWriter(aFileWriter);
		PrintWriter fileWriter = new PrintWriter(aBufferedWriter);

		// Looping through each slot item and writing a line that shows sales
		for(Slot slot : vendingMachine.slots) {
			fileWriter.println(slot.getSlotName() + "|" + (5 - slot.getRemainingItems()));
		}
		fileWriter.println("\n");
		fileWriter.println("**TOTAL SALES** $"+ vendingMachine.myFormat.format(vendingMachine.getTotalSales()));

		// Closing file
		fileWriter.close();

	}
}	// end of class



