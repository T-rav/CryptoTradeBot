/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test.to;

import com.kungfuactiongrip.exchange.to.MarketTradeFee;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class MarketTradeFeeTest {
    
    public MarketTradeFeeTest() {
    }
    
    @Test
    public void Ctor_ExpectObjectWithValues(){
        MarketTradeFee mtf = new MarketTradeFee(0.001, 0.01);
        
        assertEquals(0.001, mtf.Fee, 0.000000001);
        assertEquals(0.01, mtf.Net, 0.000000001);
    }
    
    @Test
    public void FetchTotal_ExpectFeeAndNetAdded(){
        MarketTradeFee mtf = new MarketTradeFee(0.001, 0.01);
        
        assertEquals(0.011, mtf.TotalCost(), 0.000000001);
    }
}
