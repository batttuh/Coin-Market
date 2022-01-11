/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.repo;

import data.models.CoinMarket;
import data.models.Coin;
import data.models.Trader;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author PC
 */

public class CoinMarketDatabase {
    private static final String url="jdbc:sqlite:C:/sqlite/CoinMarket.db";
    public static void createNewDatabase(String fileName) {        
        try {  
            Connection conn = DriverManager.getConnection(url);  
            if (conn != null) {  
                DatabaseMetaData meta = conn.getMetaData();  
                System.out.println("The driver name is " + meta.getDriverName());  
                System.out.println("A new database has been created.");  
            }  
   
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }    
        public static void createNewTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Ledger "  
                + "(userId TEXT NOT NULL"  
                + sqlCodeGeneraterForCoins()
                + ",fiat INTEGER DEFAULT 0"
                + ")";  
        try{
            Connection conn = DriverManager.getConnection(url);  
            Statement stmt = conn.createStatement();  
            stmt.execute(sql);  
            System.out.println("Table is created");
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    } 
        /// SEN BİR YERDE FIAT I ARTTIRIYORDUN NERE ORA
    private Connection connect() {  
        // SQLite connection string  
        String url = "jdbc:sqlite:C://sqlite/CoinMarket.db";  
        Connection conn = null;  
        try {  
            conn = DriverManager.getConnection(url);  
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
        return conn;  
    }
    // INSERT WHEN USER IS REGISTERED
    public void insert(String userId) {  
          String sql = "INSERT INTO Ledger(userId) VALUES(?)";
        try{  
            Connection conn = this.connect();  
            PreparedStatement pstmt = conn.prepareStatement(sql);  
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
            System.out.println(userId+" is added");
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
    }
    
    public void update(String coinName, int coinAmount, String userId, double currentFiat) {
        String sql = "UPDATE Ledger SET "+coinName +" = ?, fiat = ?"
                + "WHERE userId = ?";

        try (Connection conn = this.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, coinAmount);
            pstmt.setDouble(2, currentFiat); 
            pstmt.setString(3, userId);
            System.out.println("Updated");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
     public void updateFiat(String userId,double takenFiat){
         double fiat=CoinMarket.trader.getFiat()+takenFiat;
         String sql = "UPDATE Ledger SET fiat = ?"
                + "WHERE userId = ?";
         try (Connection conn = this.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setDouble(1, fiat);
            pstmt.setString(2, userId);
            CoinMarket.trader.setFiat(fiat);
            System.out.println("Updated");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
     }
     public Trader callTrader(String email, String userId){
         Trader calledTrader = new Trader(email, userId, fetchSpotWalletForTrader(userId), fetchTraderFiat(userId));
         return calledTrader;
     }
     public int getCoinAmountFromDatabaseForReceiverUser(String userId,String coinName){
         String sqlCommand = "SELECT * FROM Ledger WHERE userId = ?";
         try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlCommand)){

            pstmt.setString(1,userId);

            ResultSet rs  = pstmt.executeQuery();
            return rs.getInt(coinName);
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
         
     }
     public boolean updateReceiverUser(String userId,String coinName,int coinAmount){
         String sql = "UPDATE Ledger SET "+coinName +" = ?"
                + "WHERE userId = ?";
         int amountCoinReceiverHas=getCoinAmountFromDatabaseForReceiverUser(userId, coinName);
         if(amountCoinReceiverHas==-1){
             return false;
         }
         amountCoinReceiverHas+=coinAmount;
        try (Connection conn = this.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, amountCoinReceiverHas);
            pstmt.setString(2, userId);
            System.out.println("Updated");
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
     }

    public Map<String, Coin> fetchSpotWalletForTrader(String userId){
       String sqlCommand = "SELECT * FROM Ledger WHERE userId = ? ";
       try (
            Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sqlCommand)){
            
            // set the value
            pstmt.setString(1,userId);
         
            
            ResultSet rs  = pstmt.executeQuery();
            // loop through the result set
           return fetchSpotWalletForTraderResultHandler(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       return null;
    }
     /** TRADER SPOT WALLET*/
      private static Map<String, Coin> fetchSpotWalletForTraderResultHandler(ResultSet rs) throws SQLException{
            CoinMarket coinMarket=new CoinMarket();
            coinMarket.refreshCoins();
            Map<String,Coin> coinsTable=coinMarket.getAllCoins();
            Map<String, Coin> traderSpotWallet  = new HashMap<>();
            for(Map.Entry<String, Coin> entry : coinsTable.entrySet()){

            
            if(rs.getInt(entry.getKey())!=0){
              
                Coin selectedCoin = coinsTable.get(entry.getKey());
                traderSpotWallet.put(entry.getKey(), new Coin(CoinMarket.getAllCoins().get(entry.getKey()).getPrice(), 
                        selectedCoin.getChanges(), 
                        selectedCoin.getVolume(),
                        selectedCoin.getCapacity(), 
                        rs.getInt(entry.getKey())));
            }
            
        } 
        return traderSpotWallet;
      }
      private double fetchTraderFiat(String userId){
        String sqlCommand = "SELECT * FROM Ledger WHERE userId = ? ";
       try (
            Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sqlCommand)){
            
            // set the value
            pstmt.setString(1,userId);
            //Trader.wallet_address=userId;
     
            ResultSet rs  = pstmt.executeQuery();
 
           return rs.getInt("fiat");
 
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       return 0;
      }
     private static String sqlCodeGeneraterForCoins(){
             Map<String,Coin> coinsTable=new HashMap<String, Coin>();
        CoinMarket coinMarket=new CoinMarket();
        coinMarket.refreshCoins();
        coinsTable=coinMarket.getAllCoins();
        ArrayList<String> coinNames = new ArrayList<String>();
        String sqlCode = "";
        for(Map.Entry<String, Coin> entry : coinsTable.entrySet()){
            System.out.println(entry.getKey());
            String sqlCommand = ","+entry.getKey() + " INTEGER DEFAULT 0"; 
            
            sqlCode += sqlCommand;
        }
        return sqlCode; // Napıyoz
    }
}
