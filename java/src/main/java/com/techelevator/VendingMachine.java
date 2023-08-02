package com.techelevator;


import com.techelevator.view.Menu;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.text.DecimalFormat;

public class VendingMachine {

    /****************************************************
     * Formatters
     ****************************************************/
    // Formatter - DateTime
    final static DateTimeFormatter CUSTOM  = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
    LocalDateTime actionTime = LocalDateTime.now();
    String customTime = actionTime.format(CUSTOM);
    // Formatter - Decimal
    DecimalFormat myFormat = new DecimalFormat("##0.00");

    /****************************************************
     * Initialize
     ****************************************************/

    // Initialize Scanner
    Scanner userInput = new Scanner(System.in);

    // Instance variables
    LinkedList<Slot> slots = new LinkedList();
    private double money;
    private double totalSales = 0;

    // Constructor
    public VendingMachine() {
        this.money = 0.00;
    }

    // Getter
    public double getTotalSales() {
        return totalSales;
    }

    // Setters
    public void increaseTotalSales(double addedSale, VendingMachine vendingMachine) {
        vendingMachine.totalSales += addedSale;
    }

    // Additional Method: Add a slot to vending machine
    public void addSlot(Slot aSlot){
        slots.add(aSlot);
    }

    /****************************************************
     * Display All Items in VM
     ****************************************************/

    public void displayItems() {
        LinkedList<String> slotNumbers = new LinkedList();
        LinkedList<String> slotProducts = new LinkedList();
        LinkedList<Double> slotPrices = new LinkedList();
        LinkedList<Integer> slotQuantities = new LinkedList();

        for (int i = 0; i < slots.size(); i++) {
            Slot eachSlot = slots.get(i);
            // System.out.println(eachSlot.toString());
            slotNumbers.add(eachSlot.getID());
            slotProducts.add(eachSlot.getSlotName());
            slotPrices.add(eachSlot.getPrice());
            slotQuantities.add(eachSlot.getRemainingItems());
        }
        // Display
        int index = 0;
        for (int i = 0; i < 4; i++) {
            System.out.println("*".repeat(100));
            for (int j = 0; j < 4; j++) {
                System.out.printf("%-25s", "Slot Number: " + slotNumbers.get(index));
                index ++;
            }
            index -= 4;
            System.out.println();
            for (int j = 0; j < 4; j++) {
                System.out.printf("%-25s", "Name: " + slotProducts.get(index));
                index ++;
            }
            index -= 4;
            System.out.println();
            for (int j = 0; j < 4; j++){
                System.out.printf("%-25s", "Price: $" + slotPrices.get(index));
                index ++;
            }
            index -= 4;
            System.out.println();
            for (int j = 0; j < 4; j++){
                System.out.printf("%-25s", "Quantity: " + slotQuantities.get(index));
                index ++;
            }
            System.out.println();
        }
    }
    /********************************************************
     * 	                    PURCHASE MENU
     ********************************************************/

    /******
     Initialize instance variables for Purchase Menu
     ******/
    private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
    private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
    private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
    private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_FEED_MONEY,
            PURCHASE_MENU_OPTION_SELECT_PRODUCT,
            PURCHASE_MENU_OPTION_FINISH_TRANSACTION,
    };

    private Menu purchaseMenu;              // Menu object to be used by an instance of this class

    public VendingMachine(Menu menu) throws FileNotFoundException {  // Constructor - user will pass a menu for this class to use
        this.purchaseMenu = menu;           // Make the Menu the user object passed, our Menu
    }

    /******
     Main Operations of Purchase Menu
     ******/

    public void purchaseItems(VendingMachine vendingMachine) throws IOException {
        // Record to log
        File outputFile = new File("Log.txt");
        FileWriter aFileWriter = new FileWriter(outputFile, false);
        BufferedWriter aBufferedWriter = new BufferedWriter(aFileWriter);
        PrintWriter fileWriter = new PrintWriter(aBufferedWriter);

        // Run options
        boolean shouldProcess = true;         // Loop control variable

        while (shouldProcess) {                // Loop until user indicates they want to exit
            System.out.println("Current money provided: $" + myFormat.format(vendingMachine.money));
            String choice = (String) purchaseMenu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

            switch (choice) {                  // Process based on user menu choice
                case PURCHASE_MENU_OPTION_FEED_MONEY:
                    System.out.print("Please enter the amount to add (in dollars): $");
                    try {
                        double moneyAdd = Double.parseDouble(userInput.nextLine());
                        // Unsuccessful add: money cannot be negative
                        while (moneyAdd < 0) {
                            System.out.println("Invalid input. Try again.");
                            System.out.print("Please enter the amount to add (in dollars): $");
                            moneyAdd = Double.parseDouble(userInput.nextLine());
                        }
                        vendingMachine.feedMoney(moneyAdd, vendingMachine);
                        // Record to log
                        fileWriter.println(customTime + " FEED MONEY: $" + myFormat.format(moneyAdd) + " $" + myFormat.format(vendingMachine.money));
                    } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                    }
                    break;
                case PURCHASE_MENU_OPTION_SELECT_PRODUCT:
                    Slot productSlot = vendingMachine.selectProduct(vendingMachine);
                    // Unsuccessful purchase: item costs more than balance
                    if (productSlot.getID() != null) {
                        // Record to log
                        fileWriter.println(customTime + " " + productSlot.getSlotName() + " " + productSlot.getID() + " $" + myFormat.format(productSlot.getPrice()) + " $" + myFormat.format(vendingMachine.money));
                    }
                    break;
                case PURCHASE_MENU_OPTION_FINISH_TRANSACTION:
                    double change = vendingMachine.finishTransaction(vendingMachine);
                    // Record to log
                    fileWriter.println(customTime + " GIVE CHANGE: $" + myFormat.format(change) + " $" + myFormat.format(vendingMachine.money));
                    // Exit
                    shouldProcess = false;
                    break;
                default: continue;
            }
        }

        // Close log file
        fileWriter.close();
    }

    /******
     Purchase Menu Option 1: Feed Money
     ******/
    public void feedMoney(double money, VendingMachine vendingMachine){
        vendingMachine.money += money;
    }

    /******
     Purchase Menu Option 2: Select Product
     ******/

    // Return a list of all product IDs for search
    public LinkedList<String> getIDs(){
        LinkedList<String> itemIDs = new LinkedList();
        for (Slot slot:this.slots){
            itemIDs.add(slot.getID());
        }
        return itemIDs;
    }

    // Deduct the balance of user once purchase successfully
    public void deductMoney(double money, VendingMachine vendingMachine){
        vendingMachine.money -= money;
    }

    // Main operation to return a product selected to purchase
    public Slot selectProduct(VendingMachine vendingMachine){
        // Show the products
        this.displayItems();
        // Input
        System.out.print("Enter the product code: ");
        String productID = userInput.nextLine();
        // Linear search to check if product input exists
        boolean inList = false;
        while (inList == false){
            boolean found = false;
            for (int i = 0; i < getIDs().size(); i++){
                if (productID.equals(getIDs().get(i))){
                    found = true;
                    inList = true;
                    break;
                }
            }
            if (found == false) {
                System.out.println("The product code entered does not exist. Try again.");
                System.out.print("Enter the product code: ");
                productID = userInput.nextLine();
            }
        }
        // Initialize
        int slotIndex = 0;
        boolean isDummy = false;
        boolean isSoldOut = false;
        // Dummy slot
        Slot dummySlot = new Slot();
        // Dispense product
        for (int i = 0; i < slots.size(); i++){
            // Found productID
            // Only allow to purchase item with price <= current balance
            if (slots.get(i).getID().equals(productID)) {
                // Item costs more than balance
                if (vendingMachine.money < slots.get(i).getPrice()) {
                    isDummy = true;
                    break;
                }
                // Product is sold out
                if (slots.get(i).getRemainingItems() == 0) {

                    isDummy = true;
                    isSoldOut = true;
                    break;
                    // Dispense item
                } else {
                    slots.get(i).dispenseItem();
                    System.out.println("Item purchase: " + slots.get(i).getSlotName());
                    System.out.println("Item price: " + slots.get(i).getPrice());
                    vendingMachine.deductMoney(slots.get(i).getPrice(), vendingMachine);
                    vendingMachine.increaseTotalSales(slots.get(i).getPrice(), vendingMachine);
                    slotIndex = i;
                }
                break;
            }
        }
        if (isDummy == false) {
            return slots.get(slotIndex);
        } else if (isDummy == true && isSoldOut == false) {
            System.out.println("\nPurchase unsuccessful. Item costs more than balance.");
            return dummySlot;
        } else {
            System.out.println("SOLD OUT");
            return dummySlot;
        }
    }

    /******
     Purchase Menu Option 3: Finish Transaction
     ******/

    public double finishTransaction(VendingMachine vendingMachine){
        double change = 0.00;
        double remainingBalance = vendingMachine.money;
        final double nickel = 0.05;
        final double dime = 0.10;
        final double quarter = 0.25;
        int numberOfNickels = 0;
        int numberOfDimes = 0;
        int numberOfQuarters = 0;

        // Dispensing change using the least number of coins possible
        if (vendingMachine.money > 0){
            while(remainingBalance>=quarter) {
                change += quarter;
                remainingBalance -= quarter;
                numberOfQuarters ++;
            }
            while(remainingBalance>=dime) {
                change += dime;
                remainingBalance -= dime;
                numberOfDimes ++;
            }
            while(remainingBalance>=nickel) {
                change += nickel;
                remainingBalance -= nickel;
                numberOfNickels ++;
            }
        }
        vendingMachine.money = 0.00; // reset to 0 balance
        // Printing out change.
        System.out.println("Here is your change:" + numberOfNickels + " nickels, " + numberOfDimes + " dimes, " + numberOfQuarters + " quarters.");
        return change;
    }

}
