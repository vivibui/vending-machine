package com.techelevator;

import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class Slot {

    // Constant
    private int MAXIMUM_QUANTITY = 5;

    // Instance variables
    ArrayList<Item> items = new ArrayList(MAXIMUM_QUANTITY);
    private String ID;
    private double price;
    private String slotName;
    private String slotType;

    // Constructor
    public Slot(){
        this.ID = null; // Dummy slot
    }

    public Slot(String ID,String slotName, double price, String slotType){
        this.ID = ID;
        this.slotName = slotName;
        this.price = price;
        this.slotType = slotType;
    }

    // Getters
    public String getID(){
        return this.ID;
    }
    public double getPrice(){
        return this.price;
    }
    public ArrayList<Item> getItems(){
        return this.items;
    }
    public int getRemainingItems(){
        return this.items.size();
    }
    public String getSlotName(){
        return this.slotName;
    }
    public String getSlotType(){
        return this.slotType;
    }


    // Stock slot to maximum quantity
    public void stockSlot(Item item){
        for (int i = 0; i < MAXIMUM_QUANTITY; i++){
            items.add(item);
        }
    }

    // Additional Method
    public void dispenseItem() {
        if (items.size() > 0) {
            System.out.println(items.get(0).displayMessage());
            this.items.remove(0);
        }
    }

    // To String
    public String toString(){
        return "Slot Option: " + this.ID + "\nProduct Name: " + this.slotName + "\nPrice: " + this.price + "\nQuantity: " + this.getRemainingItems();
    }

}
