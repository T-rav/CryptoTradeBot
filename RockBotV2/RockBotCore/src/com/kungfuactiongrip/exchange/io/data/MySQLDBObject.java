/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io.data;

import com.kungfuactiongrip.exchange.ExchangeList;
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
    
    private final String insertOrder = "insert into BotTrade(tradeID, exchangeName, marketID, totalCurrencyValue, "
                                        + "btcPricePer, orderState, orderType, "
                                        + "linkedTradeID) values(?,?,?,?,?,?,?,?)";
    
    private final String updateOrder = "update BotTrade set orderState = ? where rID = ?";
    
    private final String orderStateCountInterval = "select count(rID) as 'total' from BotTrade where orderState = ? "
                                            + "and orderType = ? and exchangeName = ? and marketID = ? and "
                                            + "rTS >= now() - INTERVAL ? HOUR";
    
    private final String orderStateCount = "select count(rID) as 'total' from BotTrade where orderState = ? "
                                            + "and orderType = ? and exchangeName = ? and marketID = ?";
    
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
    
    @Override
    public boolean InsertOrder(TradeType type, TradeState state, ExchangeList exchange, int marketID, double pricePer, double totalValue, String tradeID, String linkedID) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean result = false;
        
        try{
           conn = CreateConnection();
           if(conn != null){
               ps = conn.prepareStatement(insertOrder);
               if(ps != null){
                   String typeName = type.name();
                   String stateName = state.name();
                   String exchangeName = exchange.name().toUpperCase();
                   
                   ps.setString(1, tradeID);
                   ps.setString(2, exchangeName);
                   ps.setInt(3, marketID);
                   ps.setDouble(4, totalValue);
                   ps.setDouble(5, pricePer);
                   ps.setString(6, stateName);
                   ps.setString(7, typeName);
                   ps.setString(8, linkedID);
                   
                   ps.execute();
                   result = true;
                  
               }
           }
       }catch (SQLException ex) {
            Logger.getLogger(MySQLDBObject.class.getName()).log(Level.SEVERE, null, ex);
       }finally{
            
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
    
    
    @Override
    public boolean UpdateOrderState(int rowID, TradeState state) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean result = false;
        
        try{
           conn = CreateConnection();
           if(conn != null){
               ps = conn.prepareStatement(updateOrder);
               if(ps != null){
                   String stateName = state.name();
                   
                   ps.setString(1, stateName);
                   ps.setInt(2, rowID);
                   
                   ps.execute();
                   result = true;
               }
           }
       }catch (SQLException ex) {
            Logger.getLogger(MySQLDBObject.class.getName()).log(Level.SEVERE, null, ex);
       }finally{
            
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
    
    @Override
    public int FetchNumberOfAbortedTradesForInterval(TradeType tradeType, ExchangeList exchange, int marketID, int hourInterval) {
        int result = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
           conn = CreateConnection();
           if(conn != null){
               ps = conn.prepareStatement(orderStateCountInterval);
               if(ps != null){
                   String typeName = tradeType.name();
                   String stateName = TradeState.ABORTED.name();
                   String exchangeName = exchange.name().toUpperCase();
                 
                   ps.setString(1, stateName);
                   ps.setString(2, typeName);
                   ps.setString(3, exchangeName);
                   ps.setInt(4, marketID);
                   ps.setInt(5, hourInterval);
                 
                   rs = ps.executeQuery();
                   
                   if(rs.next()){
                       result = rs.getInt("total");
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
    
    
    @Override
    public int FetchOpenOrderCount(TradeType tradeType, ExchangeList exchange, int marketID) {
         int result = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
           conn = CreateConnection();
           if(conn != null){
               ps = conn.prepareStatement(orderStateCount);
               if(ps != null){
                   String typeName = tradeType.name();
                   String stateName = TradeState.OPEN.name();
                   String exchangeName = exchange.name().toUpperCase();
                 
                   ps.setString(1, stateName);
                   ps.setString(2, typeName);
                   ps.setString(3, exchangeName);
                   ps.setInt(4, marketID);
                 
                   rs = ps.executeQuery();
                   
                   if(rs.next()){
                       result = rs.getInt("total");
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
