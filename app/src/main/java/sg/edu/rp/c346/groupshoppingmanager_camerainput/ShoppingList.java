package sg.edu.rp.c346.groupshoppingmanager_camerainput;

import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingList implements Serializable {

    String name;
    double price;
    int qty;
    int id;

    /* Constructor to store data */
    public ShoppingList(int id,String name, double price, int qty)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public double getPrice() {

        return price;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    public int getQty() {

        return qty;
    }

    public void setQty(int qty) {

        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Item " + id +
                ": name=" + name +
                ", price=" + price +
                ", quantity=" + qty + "\n";
    }

}

