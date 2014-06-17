/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.to.BuyOrder;
import com.kungfuactiongrip.to.SellOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
class MySQLDBObject implements IBotIO {

     // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/EMP";

   //  Database credentials
   static final String USER = "username";
   static final String PASS = "password";
    
    MySQLDBObject() {
        this(null);
    }
    
    MySQLDBObject(String propertyFileName){
        
        if(propertyFileName != null){
            // TODO : Do something ;)
        }
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
