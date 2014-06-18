/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.io.IBotIO;
import com.kungfuactiongrip.exchange.io.IOFactory;
import com.kungfuactiongrip.to.BuyOrder;
import com.kungfuactiongrip.to.SellOrder;
import com.kungfuactiongrip.to.TradeOrder;
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
        IBotIO botIO = IOFactory.CreateIOObject();
        
        assertNotNull(botIO);
    }
    
    @Test
    public void CanCreateBotIOObject_WithPropertiesFile_ExpectValidObject(){
        IBotIO botIO = CreateIOObject();
        
        assertNotNull(botIO);
    }
    
    @Test
    public void CanCreateBotIOObject_WithNullPropertiesFile_ExpectNull(){
        IBotIO botIO = IOFactory.CreateIOObject(null);
        
         assertNull(botIO);
    }
    
    @Test
    public void CanFetchOpenBuyOrders_WithMarketIDWhereNoOrder_ExpectNoOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenBuyOrdersForMarket(174, ExchangeList.Cryptsy);
        
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void CanFetchOpenSellOrders_WithMarketIDWhereNoOrder_ExpectNoOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenSellOrdersForMarket(174, ExchangeList.Cryptsy);
        
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void CanFetchOpenBuyOrders_WithMarketIDWhereOrder_ExpectOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenBuyOrdersForMarket(173, ExchangeList.Cryptsy);
        
        // Assert
        assertFalse(orders.isEmpty());
        TradeOrder order = orders.get(0);
        assertEquals(1, order.RowID);
        assertEquals(0.1, order.PricePer, 0.00000001);
        assertEquals("CRYPTSY", order.Exchange.name().toUpperCase());
        assertEquals("Dummy-1", order.TradeID);
    }
    
    @Test
    public void CanFetchOpenSellOrders_WithMarketIDWhereOrder_ExpectOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenSellOrdersForMarket(173, ExchangeList.Cryptsy);
        
        // Assert
        assertFalse(orders.isEmpty());
        TradeOrder order = orders.get(0);
        assertEquals(2, order.RowID);
        assertEquals(0.1, order.PricePer, 0.00000001);
        assertEquals("CRYPTSY", order.Exchange.name().toUpperCase());
        assertEquals("Dummy-2", order.TradeID);
    }
    
    /*** Helper Methods ***/
    private IBotIO CreateIOObject() {
        IBotIO botIO = IOFactory.CreateIOObject("Test_DB");
        return botIO;
    }
    
    
}
