/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io.data;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.to.BuyOrder;
import com.kungfuactiongrip.to.SellOrder;
import com.kungfuactiongrip.to.TradeOrder;
import com.kungfuactiongrip.to.TradeOrderFactory;
import com.kungfuactiongrip.to.TradeState;
import com.kungfuactiongrip.to.TradeType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Administrator
 */
public class MySQLDBObject implements IDbDAO {

    // JDBC driver name and database URL
    String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    String DB_URL = "";
    //  Database credentials
    String USER = "username";
    String PASS = "password";
    
    private final String fetchBuyOrders = "select rID, tradeID, btcPricePer, linkedTradeID, rTS "
                                          + " from BotTrade where orderState = ? and orderType = ? "
                                          + "and marketID = ? and exchangeName = ?";
    
    
    protected MySQLDBObject(){
        this("url", "username", "password");
    }
    
    protected MySQLDBObject(String url, String user, String pass){
        DB_URL = url;
        USER = user;
        PASS = pass;
    }

    @Override
    public String FetchUser(){
        return USER;
    }
    
    @Override
    public Connection CreateConnection(){
        Connection conn = null;
        
        try{
           Class.forName(JDBC_DRIVER);
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
        }catch(SQLException | ClassNotFoundException se){
            Logger.getLogger(MySQLDBObject.class.getName()).log(Level.SEVERE, null, se);
        }
        
        return conn;
    }
    
    @Override
    public List<TradeOrder> FetchOpenBuyOrdersForMarket(int marketID, ExchangeList exchange) {
       return FetchOrders(TradeType.BUY, TradeState.OPEN, marketID, exchange);
    }

    @Override
    public List<TradeOrder> FetchOpenSellOrdersForMarket(int marketID, ExchangeList exchange) {
        return FetchOrders(TradeType.SELL, TradeState.OPEN, marketID, exchange);
    }
    
    // Generic Fetch Orders Method ;)
    private List<TradeOrder> FetchOrders(TradeType type, TradeState state, int marketID, ExchangeList exchange) {
       
        List<TradeOrder> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
           conn = CreateConnection();
           if(conn != null){
               ps = conn.prepareStatement(fetchBuyOrders);
               if(ps != null){
                   String typeName = type.name();
                   String stateName = state.name();
                   String exchangeName = exchange.name().toUpperCase();
                   
                   ps.setString(1, stateName);
                   ps.setString(2, typeName);
                   
                   ps.setInt(3, marketID);
                   ps.setString(4, exchangeName);
                   
                   rs = ps.executeQuery();
                   
                   while(rs.next()){
                       int rowID = rs.getInt("rID");
                       String tradeID = rs.getString("tradeID");
                       double pricePer = rs.getDouble("btcPricePer");
                       String linkedTradeID = rs.getString("linkedTradeID");
                       String rowTS = rs.getString("rTS");
                       
                       // add to result collection ;)
                       result.add(TradeOrderFactory.GenerateOrder(type, rowID, tradeID, pricePer, linkedTradeID, exchange, rowTS));
                   }
               }
           }
       }catch (SQLException ex) {
            Logger.getLogger(MySQLDBObject.class.getName()).log(Level.SEVERE, null, ex);
       }finally{
            // close result set
            try {
                if(rs != null && !rs.isClosed()){
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(MySQLDBObject.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(MySQLDBObject.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // close the prepared statement
            if(ps != null ){
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MySQLDBObject.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //close the connection
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MySQLDBObject.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
       return result;
    }
    
}
