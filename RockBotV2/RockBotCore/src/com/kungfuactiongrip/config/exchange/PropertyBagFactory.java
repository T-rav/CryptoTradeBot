/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.config.exchange;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class PropertyBagFactory {
    
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    public static PropertyBag GenerateFromConfig(String config){
        InputStream stream = null;
        PropertyBag result = new PropertyBag();
        
        try{
            Properties prop = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();       
            stream = loader.getResourceAsStream("com/kungfuactiongrip/config/"+config+".properties");
            prop.load(stream);

            Enumeration<?> propertyNames = prop.propertyNames();
            while(propertyNames.hasMoreElements()){
                Object next = propertyNames.nextElement();
                String key = next.toString();
                String value = prop.getProperty(key);
                result.AddPair(key, value);
            }
        }catch(Exception e){
            Logger.getGlobal().log(Level.WARNING, config, e);
            return null;
        }finally{
            if(stream != null){
                try{
                    stream.close();
                }catch(IOException e){
                    Logger.getGlobal().log(Level.WARNING, config, e);
                }
            }
        }
        
        return result;
    }
}
