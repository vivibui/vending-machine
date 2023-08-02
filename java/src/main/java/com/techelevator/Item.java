package com.techelevator;

public abstract class Item{

    // Instance variable
    private String type;
    private String name;
    private String message;

    // Constructor
    public Item(String type, String name){
        this.type = type;
        this.name = name;
    }

    // Getters
    public String getType(){
        return this.type;
    }
    public String getName(){
        return this.name;
    }
    public String displayMessage(){
        return this.message;
    }

    // Setters
    public void setMessage(String newMessage){
        this.message = newMessage;
    }

    // To String
    public String toString(){
        return "The item type: " + this.type + "\nThe item name: " + this.name;
    }
}
