/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test.to;

import com.kungfuactiongrip.exchange.objects.MarketSellOrder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class MarketSellOrderTest {
    
    public MarketSellOrderTest() {
    }
    
    @Test
    public void CompareTo_ExpectEquals(){
        MarketSellOrder mso1 = new MarketSellOrder(0.01, -1, -1);
        MarketSellOrder mso2 = new MarketSellOrder(0.01, -1, -1);
        
        int result = mso1.compareTo(mso2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void CompareTo_ExpectGreaterThan(){
        MarketSellOrder mso1 = new MarketSellOrder(0.001, -1, -1);
        MarketSellOrder mso2 = new MarketSellOrder(0.01, -1, -1);
        
        int result = mso1.compareTo(mso2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void CompareTo_ExpectLessThan(){
        MarketSellOrder mso1 = new MarketSellOrder(0.01, -1, -1);
        MarketSellOrder mso2 = new MarketSellOrder(0.001, -1, -1);
        
        int result = mso1.compareTo(mso2);
        
        assertEquals(-1, result);
    }
}
