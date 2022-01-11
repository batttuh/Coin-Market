/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.models;

/**
 *
 * @author PC
 */
public class Coin {
    private double price;
    private double changes;
    private double volume;
    private double capacity;
    private int coinAmountUserHas = 0;
    public Coin(double price, double changes, double volume, double capacity) {
        this.price = price;
        this.changes = changes;
        this.volume = volume;
        this.capacity = capacity;
    }
    public Coin(double price, double changes, double volume, double capacity, int coinAmountUserHas){
        this.price = price;
        this.changes = changes;
        this.volume = volume;
        this.capacity = capacity;
        this.coinAmountUserHas = coinAmountUserHas;
    }
    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the changes
     */
    public double getChanges() {
        return changes;
    }

    /**
     * @return the volume
     */
    public double getVolume() {
        return volume;
    }

    /**
     * @return the capacity
     */
    public double getCapacity() {
        return capacity;
    }
    
    public int getCoinAmountUserHas(){
        return coinAmountUserHas;
    }
    public void changeCoinAmount(int amount){
        this.coinAmountUserHas += amount;
    }
    
}
