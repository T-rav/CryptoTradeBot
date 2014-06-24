/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test.to;

import com.kungfuactiongrip.exchange.to.MarketCancelOrderResult;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class MarketCancelOrderResultTest {
    
    public MarketCancelOrderResultTest() {
    }
    
    @Test
    public void Ctor_WhenErrorTrue_ExpectErrorObject(){
        MarketCancelOrderResult mcor = new MarketCancelOrderResult("An Error Happened", true);
        
        assertEquals("An Error Happened", mcor.Message);
        assertTrue(mcor.IsError);
    }
    
    @Test
    public void Ctor_WhenErrorFalse_ExpectNormalObject(){
        MarketCancelOrderResult mcor = new MarketCancelOrderResult("Happy Message", false);
        
        assertEquals("Happy Message", mcor.Message);
        assertFalse(mcor.IsError);
    }
}
