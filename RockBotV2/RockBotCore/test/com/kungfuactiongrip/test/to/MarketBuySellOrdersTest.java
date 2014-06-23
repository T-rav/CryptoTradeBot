/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test.to;

import com.kungfuactiongrip.exchange.objects.MarketBuyOrder;
import com.kungfuactiongrip.exchange.objects.MarketBuySellOrders;
import com.kungfuactiongrip.exchange.objects.MarketSellOrder;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class MarketBuySellOrdersTest {
    
    public MarketBuySellOrdersTest() {
    }
    
    @Test
    public void FetchBestBuyPrice_WhenEmpty_ExpectNegativeOne(){
        MarketBuySellOrders mbso = new MarketBuySellOrders();
        
        double result = mbso.FetchBestBuyPrice();
        assertEquals(-1, result,0.0000000001);
    }
    
    @Test
    public void FetchBestSellPrice_WhenEmpty_ExpectNegativeOne(){
        MarketBuySellOrders mbso = new MarketBuySellOrders();
        
        double result = mbso.FetchBestSellPrice();
        assertEquals(-1, result,0.0000000001);
    }
    
    @Test
    public void FetchBestBuyPrice_ExpectBestPriceReturned(){
        MarketBuySellOrders mbso = new MarketBuySellOrders();
        
        MarketBuyOrder order1 = new MarketBuyOrder(0.01,10,0.1);
        MarketBuyOrder order2 = new MarketBuyOrder(0.015,10,0.15);
        
        mbso.BuyOrders.add(order1);
        mbso.BuyOrders.add(order2);
        
        double result = mbso.FetchBestBuyPrice();
        assertEquals(0.015, result,0.0000000001);
    }
    
    @Test
    public void FetchBestSellPrice_ExpectBestPriceReturned(){
        MarketBuySellOrders mbso = new MarketBuySellOrders();
        
        MarketSellOrder order1 = new MarketSellOrder(0.01,10,0.1);
        MarketSellOrder order2 = new MarketSellOrder(0.015,10,0.15);
        
        mbso.SellOrders.add(order1);
        mbso.SellOrders.add(order2);
        
        double result = mbso.FetchBestSellPrice();
        assertEquals(0.015, result,0.0000000001);
    }
}
