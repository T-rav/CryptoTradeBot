/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test;

import com.kungfuactiongrip.to.SellOrder;
import com.kungfuactiongrip.exchange.io.*;
import com.kungfuactiongrip.to.BuyOrder;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class IOTest {
    
    public IOTest() {
    }
    

    @Test
    public void CanCreateBotIOObject_ExpectValidObject(){
        IBotIO botIO = IOFactory.CreateDBObject();
        
        assertNotNull(botIO);
    }
    
    @Test
    public void CanCreateBotIOObject_WithPropertiesFile_ExpectPropertiesConfigured(){
        IBotIO botIO = IOFactory.CreateDBObject("testing.db");
        
    }
    
    @Test
    public void CanFetchOpenBuyOrders_ExpectNoErrorOrOrders(){
        IBotIO botIO = IOFactory.CreateDBObject();
        
        List<BuyOrder> orders = botIO.FetchOpenBuyOrdersForMarket(173);
        
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void CanFetchOpenSellOrders_ExpectNoErrorOrOrders(){
        IBotIO botIO = IOFactory.CreateDBObject();
        
        List<SellOrder> orders = botIO.FetchOpenSellOrdersForMarket(173);
        
        assertTrue(orders.isEmpty());
    }
    
}
