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
public abstract class TradeOrder {

    public final int RowID;
    public String TradeID;
    public double PricePer;
    public String LinkedTradeID;
    public final ExchangeList Exchange;
    public final String TradeTS;
    
    public TradeOrder(){
        this(-1, "",0.0,"",ExchangeList.Cryptsy,"");
    }
    
    public TradeOrder(int rowID, String tradeID, double pricePer, String linkedID, ExchangeList exchange, String tradeTS){
        RowID = rowID;
        TradeID = tradeID;
        PricePer = pricePer;
        LinkedTradeID = linkedID;
        Exchange = exchange;
        TradeTS = tradeTS;
    }
    
    public abstract TradeType TradeType();
    
    public void SetTradeID(String tradeID){
        TradeID = tradeID;
    }
    
    public void SetPricePer(double amt){
        PricePer = amt;
    }
    
    public void SetLinkedTradeID(String tradeID){
        LinkedTradeID = tradeID;
    }

}
