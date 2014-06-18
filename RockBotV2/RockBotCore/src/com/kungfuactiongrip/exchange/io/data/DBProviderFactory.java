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
public class DBProviderFactory {
    
    public static IDbDAO GenerateMySQLDBObject(){
       return GenerateMySQLDBObject(null);
    }
    
    public static IDbDAO GenerateMySQLDBObject(String propertyFileName){
        
         if(propertyFileName != null && !"".equals(propertyFileName)){
            PropertyBag bg = PropertyBagFactory.GenerateFromConfig(propertyFileName);
            String url = bg.FetchKey("DB_URL");
            String user = bg.FetchKey("USER");
            String pass = bg.FetchKey("PASS");
            return new MySQLDBObject(url, user, pass);
        }
         
         return new MySQLDBObject();
    }
}
