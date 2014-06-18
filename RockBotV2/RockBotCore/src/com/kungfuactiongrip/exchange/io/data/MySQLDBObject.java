/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io.data;

import com.kungfuactiongrip.to.BuyOrder;
import com.kungfuactiongrip.to.SellOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


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
            se.printStackTrace();
        }
        
        return conn;
    }
    
    @Override
    public List<BuyOrder> FetchOpenBuyOrdersForMarket(int marketID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SellOrder> FetchOpenSellOrdersForMarket(int marketID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
