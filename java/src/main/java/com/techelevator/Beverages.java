package com.techelevator;

public class Beverages extends Item {
    // Constructor
    public Beverages(String type, String name){
        super(type, name);
        super.setMessage("Glug Glug, Yum!");
    }
}
