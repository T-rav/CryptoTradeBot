/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.to;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MarketBuySellOrders {
    
    public List<MarketSellOrder> SellOrders;
    
    public List<MarketBuyOrder> BuyOrders;
    
    public MarketBuySellOrders(){
        SellOrders = new ArrayList<>();
        BuyOrders = new ArrayList<>();
    }
    
    public double FetchBestBuyPrice(){
        if(BuyOrders.isEmpty()){
            return -1;
        }
        
        Collections.sort(BuyOrders);
        
        return BuyOrders.get(0).Price;
    }
    
    public double FetchBestSellPrice(){
        if(SellOrders.isEmpty()){
            return -1;
        }
        
        Collections.sort(SellOrders);
        
        return SellOrders.get(0).Price;
    }
}
