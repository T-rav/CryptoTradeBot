/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test.to;

import com.kungfuactiongrip.exchange.to.MarketBuyOrder;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class MarketBuyOrderTest {
    
    public MarketBuyOrderTest() {
    }

    @Test
    public void CompareTo_ExpectEquals(){
        MarketBuyOrder mso1 = new MarketBuyOrder(0.01,-1,-1);
        MarketBuyOrder mso2 = new MarketBuyOrder(0.01,-1,-1);

        int result = mso1.compareTo(mso2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void CompareTo_ExpectGreaterThan(){
        MarketBuyOrder mso1 = new MarketBuyOrder(0.001,-1,-1);
        MarketBuyOrder mso2 = new MarketBuyOrder(0.01,-1,-1);
        
        int result = mso1.compareTo(mso2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void CompareTo_ExpectLessThan(){
        MarketBuyOrder mso1 = new MarketBuyOrder(0.01,-1,-1);
        MarketBuyOrder mso2 = new MarketBuyOrder(0.001,-1,-1);
        
        int result = mso1.compareTo(mso2);
        
        assertEquals(-1, result);
    }
}
