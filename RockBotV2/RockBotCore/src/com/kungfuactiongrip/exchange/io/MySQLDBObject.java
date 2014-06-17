/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.config.exchange.PropertyBag;
import com.kungfuactiongrip.config.exchange.PropertyBagFactory;
import com.kungfuactiongrip.to.BuyOrder;
import com.kungfuactiongrip.to.SellOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MySQLDBObject implements IBotIO {

     // JDBC driver name and database URL
   String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   String DB_URL = "";
   //  Database credentials
   String USER = "username";
   String PASS = "password";
    
    protected MySQLDBObject() {
        this(null);
    }
    
    /**
     *
     * @param propertyFileName
     */
    protected MySQLDBObject(String propertyFileName){
        
        if(propertyFileName != null){
            PropertyBag bg = PropertyBagFactory.GenerateFromConfig(propertyFileName);
            DB_URL = bg.FetchKey("DB_URL");
            USER = bg.FetchKey("USER");
            PASS = bg.FetchKey("PASS");
        }
    }

    public String FetchUser(){
        return USER;
    }
    
    @Override
    public List<BuyOrder> FetchOpenBuyOrdersForMarket(int marketID) {
        return new ArrayList<>();
    }

    @Override
    public List<SellOrder> FetchOpenSellOrdersForMarket(int marketID) {
        return new ArrayList<>();
    }

    
}
