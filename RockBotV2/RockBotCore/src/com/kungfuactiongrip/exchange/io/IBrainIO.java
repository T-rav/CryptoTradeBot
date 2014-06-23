/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.objects.MarketBuySellOrders;

/**
 *
 * @author Administrator
 */
public interface IBrainIO {
    
    /**
     *
     * @param exchange
     * @param orderData
     * @return
     */
    public int InsertMarketOrderData(ExchangeList exchange, MarketBuySellOrders orderData);
}
