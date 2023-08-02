package com.techelevator;

public class Candy extends Item {
    // Constructor
    public Candy(String type, String name){
        super(type, name);
        super.setMessage("Munch Munch, Yum!");
    }
}
