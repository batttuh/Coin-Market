/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.models;

import UI.UIMenu;
import data.repo.CoinMarketDatabase;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hasan
 */
public class Trader extends User implements IWallet {
    private  String wallet_address = "SOME ID";
    private Map<String, Coin> spotWallet = new HashMap<>();
    private double fiat = 0; // Kullanıcı yükleme yapacağı zaman bura artacak, ama aslında fiatı yok yanlış yapıyoz 0 olmalı şimdi aynen benim hatam farkettim

    public Trader(String email, String walletAddress, Map<String, Coin> spotWallet, double fiat) {
        super(email, walletAddress);
        this.wallet_address = walletAddress;
        this.spotWallet=spotWallet;
        this.fiat=fiat;
    }
    public Trader(String email, String walletAddress) {
        super(email, walletAddress);
        this.wallet_address = walletAddress;
    }
    @Override
    public String getWalletAddress() {
        return wallet_address;
    }
    @Override
    public double getFiat(){
        return fiat;
    }
    @Override
    public void setFiat(double newFiat){
        this.fiat = newFiat;
    }
    @Override
    public Map<String, Coin> getSpotWallet() { // lan ne oluyoır ammk
        return spotWallet;
    }

    @Override
    public boolean addCoinToWallet(String coin_name, int amount) {
        fiat-=CoinMarket.getAllCoins().get(coin_name).getPrice()*amount;
        if(spotWallet.get(coin_name)!=null){
            UIMenu.jComboBox1.addItem(coin_name);
            System.out.println("why this is not adding");
            int newAmount = spotWallet.get(coin_name).getCoinAmountUserHas() + amount;
            spotWallet.get(coin_name).changeCoinAmount(amount);
            spotWallet.replace(coin_name, spotWallet.get(coin_name)); //yok senin ki daha iyi
            CoinMarketDatabase database = new CoinMarketDatabase();
            database.update(coin_name, newAmount, wallet_address, fiat);
        }else{
            Coin newCoin =  CoinMarket.allCoins.get(coin_name);
            newCoin.changeCoinAmount(amount);
            spotWallet.put(coin_name,newCoin);
            System.out.println(spotWallet.get(coin_name));
            CoinMarketDatabase database = new CoinMarketDatabase();
            database.update(coin_name, amount, wallet_address, fiat);
        }
        return true;
    }

    @Override
    public boolean sellCoin(String coin_name,int amount) {
        
        fiat+=CoinMarket.getAllCoins().get(coin_name).getPrice()*amount;
        int newAmount=spotWallet.get(coin_name).getCoinAmountUserHas()-amount;
        CoinMarketDatabase coinMarketDatabase=new CoinMarketDatabase();
        coinMarketDatabase.update(coin_name, newAmount, wallet_address, fiat);
        if(newAmount==0){
            spotWallet.remove(coin_name);
            return false;
        }else{
            spotWallet.get(coin_name).changeCoinAmount(-amount);
            spotWallet.replace(coin_name, spotWallet.get(coin_name));
            return true;
        }

    }
    @Override
    public boolean sendCoin(String coin_name,int amount,String receiverUserId) {
        int newAmount=spotWallet.get(coin_name).getCoinAmountUserHas()-amount;
        CoinMarketDatabase coinMarketDatabase=new CoinMarketDatabase();
        coinMarketDatabase.update(coin_name, newAmount, wallet_address, fiat);
        
        if(coinMarketDatabase.updateReceiverUser(receiverUserId, coin_name, amount)){
            if(newAmount==0){
            spotWallet.remove(coin_name);
            
        }   else{
            spotWallet.get(coin_name).changeCoinAmount(-amount);
            spotWallet.replace(coin_name,  spotWallet.get(coin_name));
        }
            return true;
        }else{
            return false;
        }
        

    }
    
    
}
