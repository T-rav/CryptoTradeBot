/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.trade;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.io.IBotIO;
import com.kungfuactiongrip.exchange.io.IOFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class TradeFactory {

    public static TradeRule CreateScalpTradeRule() {
        return new ScalpTradeRule();
    }

    public static ITradeBot CreateBot(ExchangeList exchange, String propertiesFile) {
        if(exchange == null || propertiesFile == null){
            return null;
        }
        
        try{
            IBotIO dbObj = IOFactory.CreateIOObject(propertiesFile, exchange);
            if(dbObj != null){
                return new TradeBotImpl(exchange, dbObj);
            }
        }catch(Exception e){
            Logger.getLogger(TradeFactory.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return null;
    }
    
}
