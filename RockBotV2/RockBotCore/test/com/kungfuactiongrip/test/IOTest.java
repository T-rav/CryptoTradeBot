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
    public void CanCreateBotIOObject_WithPropertiesFile_ExpectValidObject(){
        IBotIO botIO = CreateIOObject();
        
        assertNotNull(botIO);
    }
    
    @Test
    public void CanCreateBotIOObject_WithNullPropertiesFile_ExpectNull(){
        IBotIO botIO = IOFactory.CreateDBObject(null);
        
         assertNull(botIO);
    }
    
    @Test
    public void CanFetchOpenBuyOrders_WithMarketIDWhereNoOrder_ExpectNoOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<BuyOrder> orders = botIO.FetchOpenBuyOrdersForMarket(173);
        
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void CanFetchOpenSellOrders_WithMarketIDWhereNoOrder_ExpectNoOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<SellOrder> orders = botIO.FetchOpenSellOrdersForMarket(173);
        
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void CanFetchOpenBuyOrders_WithMarketIDWhereOrder_ExpectFiveOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<BuyOrder> orders = botIO.FetchOpenBuyOrdersForMarket(173);
        
        assertFalse(orders.isEmpty());
    }
    
    @Test
    public void CanFetchOpenSellOrders_WithMarketIDWhereOrder_ExpectFiveOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<SellOrder> orders = botIO.FetchOpenSellOrdersForMarket(173);
        
        assertFalse(orders.isEmpty());
    }
    
    /*** Helper Methods ***/
    private IBotIO CreateIOObject() {
        IBotIO botIO = IOFactory.CreateDBObject("testing_db");
        return botIO;
    }
    
    
}
