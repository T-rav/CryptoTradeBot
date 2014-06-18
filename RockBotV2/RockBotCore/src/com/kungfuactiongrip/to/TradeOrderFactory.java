/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.to;

import com.kungfuactiongrip.exchange.ExchangeList;

/**
 *
 * @author Administrator
 */
public class TradeOrderFactory {

    public static TradeOrder GenerateOrder(TradeType tradeType) {
        
        if(tradeType == TradeType.BUY){
            return new BuyOrder();
        }else if(tradeType == TradeType.SELL){
            return new SellOrder();
        }
        
        return null;
    }

    public static TradeOrder GenerateOrder(TradeType tradeType, int rowID, String tradeID, double pricePer, String linkedID, ExchangeList exchange, String rowTS) {

        if(tradeType == TradeType.BUY){
            return new BuyOrder(rowID, tradeID, pricePer, linkedID, exchange,  rowTS );
        }else if(tradeType == TradeType.SELL){
            return new SellOrder(rowID, tradeID, pricePer, linkedID, exchange, rowTS);
        }
        
        return null;
    }
    
}
