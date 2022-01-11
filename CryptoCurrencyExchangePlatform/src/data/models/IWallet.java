package data.models;

import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFil
eSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hasan
 */
interface IWallet {
    Map<String, Coin> getSpotWallet();
    boolean addCoinToWallet(String coin_name, int amount);
    boolean sellCoin(String coin_name, int amount);
    boolean sendCoin(String coin_name,int amount,String receiverUserId);
    String getWalletAddress();
    double getFiat();
    void setFiat(double newFiat);
}
