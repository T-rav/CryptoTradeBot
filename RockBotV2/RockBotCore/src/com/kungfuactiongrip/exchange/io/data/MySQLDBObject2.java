/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io.data;

import com.kungfuactiongrip.config.exchange.PropertyBag;
import com.kungfuactiongrip.config.exchange.PropertyBagFactory;

/**
 *
 * @author Administrator
 */
public class MySQLDBObject2 implements IDbDAO {

     // JDBC driver name and database URL
   String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   String DB_URL = "";
   //  Database credentials
   String USER = "username";
   String PASS = "password";
    
    protected MySQLDBObject2() {
        this(null);
    }
    
    /**
     *
     * @param propertyFileName
     */
    protected MySQLDBObject2(String propertyFileName){
        
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

}
