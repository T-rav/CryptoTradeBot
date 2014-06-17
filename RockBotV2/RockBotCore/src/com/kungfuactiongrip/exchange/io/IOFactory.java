/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io;

/**
 *
 * @author Administrator
 */
public class IOFactory {

    public static IBotIO CreateDBObject(){
        return new MySQLDBObject();
    }    

    public static IBotIO CreateDBObject(String propertyFile){
        // Use the properties file passed in to create the db stuff against ;)
        if(propertyFile == null){
            return null;
        }
        
        return new MySQLDBObject(propertyFile);
    }
}
