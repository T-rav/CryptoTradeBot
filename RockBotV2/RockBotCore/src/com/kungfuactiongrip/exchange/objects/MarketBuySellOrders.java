/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.objects;

import java.util.ArrayList;
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
}
