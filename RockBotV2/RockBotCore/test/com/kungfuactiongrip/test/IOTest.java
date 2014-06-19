/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.io.IBotIO;
import com.kungfuactiongrip.exchange.io.IOFactory;
import com.kungfuactiongrip.exchange.io.data.MySQLDBObjectTest;
import com.kungfuactiongrip.to.TradeOrder;
import com.kungfuactiongrip.to.TradeState;
import com.kungfuactiongrip.to.TradeType;
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
    public void CreateBotIOObject_ExpectValidObject(){
        IBotIO botIO = IOFactory.CreateIOObject();
        
        // Assert
        assertNotNull(botIO);
    }
    
    @Test
    public void CreateBotIOObject_WithPropertiesFile_ExpectValidObject(){
        IBotIO botIO = CreateIOObject();
        
        // Assert
        assertNotNull(botIO);
    }
    
    @Test
    public void CreateBotIOObject_WithNullPropertiesFile_ExpectValidObject(){
        IBotIO botIO = IOFactory.CreateIOObject(null);
        
        // Assert
        assertNotNull(botIO);
    }
    
    @Test
    public void FetchUser_WithInvalidDB_ExpectEmptyUser(){
        IBotIO botID = CreateBadIOObject();
        
        String user = botID.FetchUser();
        
        // Assert
        assertEquals("", user);
    }
    
    @Test
    public void FetchOpenBuyOrders_WithMarketIDWhereNoOrder_ExpectNoOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenBuyOrdersForMarket(MySQLDBObjectTest.InvalidOrderMarketID, ExchangeList.Cryptsy);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
        @Test
    public void FetchOpenBuyOrders_WithMarketIDWhereNoOrderAndInvalidDB_ExpectNoOrders(){
        IBotIO botIO = CreateBadIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenBuyOrdersForMarket(MySQLDBObjectTest.InvalidOrderMarketID, ExchangeList.Cryptsy);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void FetchOpenSellOrders_WithMarketIDWhereNoOrder_ExpectNoOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenSellOrdersForMarket(MySQLDBObjectTest.InvalidOrderMarketID, ExchangeList.Cryptsy);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void FetchOpenSellOrders_WithMarketIDWhereNoOrderAndInvalidDB_ExpectNoOrders(){
        IBotIO botIO = CreateBadIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenSellOrdersForMarket(MySQLDBObjectTest.InvalidOrderMarketID, ExchangeList.Cryptsy);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void FetchOpenBuyOrders_WithMarketIDWhereOrder_ExpectOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenBuyOrdersForMarket(MySQLDBObjectTest.ValidOrderMarketID, ExchangeList.Cryptsy);
        
        // Assert
        assertFalse(orders.isEmpty());
        TradeOrder order = orders.get(0);
        assertEquals(1, order.RowID);
        assertEquals(0.1, order.PricePer, 0.00000001);
        assertEquals("CRYPTSY", order.Exchange.name().toUpperCase());
        assertEquals("Dummy-1", order.TradeID);
    }
    
    @Test
    public void FetchOpenBuyOrders_WithMarketIDWhereOrderAndInvalidDB_ExpectNoOrders(){
        IBotIO botIO = CreateBadIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenBuyOrdersForMarket(MySQLDBObjectTest.ValidOrderMarketID, ExchangeList.Cryptsy);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void FetchOpenSellOrders_WithMarketIDWhereOrder_ExpectOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenSellOrdersForMarket(MySQLDBObjectTest.ValidOrderMarketID, ExchangeList.Cryptsy);
        
        // Assert
        assertFalse(orders.isEmpty());
        TradeOrder order = orders.get(0);
        assertEquals(2, order.RowID);
        assertEquals(0.1, order.PricePer, 0.00000001);
        assertEquals("CRYPTSY", order.Exchange.name().toUpperCase());
        assertEquals("Dummy-2", order.TradeID);
    }
    
    @Test
    public void FetchOpenSellOrders_WithMarketIDWhereOrderAndInvalidDB_ExpectNoOrders(){
        IBotIO botIO = CreateBadIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenSellOrdersForMarket(MySQLDBObjectTest.ValidOrderMarketID, ExchangeList.Cryptsy);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void InsertOrder_WhenLinkedID_ExpectValidRowID(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.InsertOrder(TradeType.BUY, TradeState.OPEN, ExchangeList.Cryptsy, MySQLDBObjectTest.InsertOrderMarketID, 0.01, 0.1, "DUMMY-INSERT", "DUMMY-LINK");
        
        // Assert
         assertTrue(result > 0);
    }
    
    @Test
    public void InsertOrder_WhenNullDBObjectWithLinkedTradeID_ExpectBadRowId(){
        IBotIO botIO = CreateBadIOObject();
        
        int result = botIO.InsertOrder(TradeType.BUY, TradeState.OPEN, ExchangeList.Cryptsy, MySQLDBObjectTest.InsertOrderMarketID, 0.01, 0.1, "DUMMY-INSERT", "DUMMY-LINK");
        
        // Assert
         assertTrue(result < 0);
    }
    
    @Test
    public void InsertOrder_WhenNoLinkedID_ExpectValidRowID(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.InsertOrder(TradeType.BUY, TradeState.OPEN, ExchangeList.Cryptsy, MySQLDBObjectTest.InsertOrderMarketID, 0.01, 0.1, "DUMMY-INSERT");
        
        // Assert
         assertTrue(result > 0);
    }
    
    @Test
    public void InsertOrder_WhenNullDBObjectWithNoLinkedTradeID_ExpectBadRowId(){
        IBotIO botIO = CreateBadIOObject();
        
        int result = botIO.InsertOrder(TradeType.BUY, TradeState.OPEN, ExchangeList.Cryptsy, MySQLDBObjectTest.InsertOrderMarketID, 0.01, 0.1, "DUMMY-INSERT");
        
        // Assert
        assertTrue(result < 0);
    }
    
    @Test
    public void UpdateOrderState_WhenOrderPresent_ExpectTrue(){
        IBotIO botIO = CreateIOObject();
        
        boolean result = botIO.UpdateOrderState(23, TradeState.CLOSED);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void UpdateOrderState_WhenOrderPresentAndBadDB_ExpectFalse(){
        IBotIO botIO = CreateBadIOObject();
        
        boolean result = botIO.UpdateOrderState(23, TradeState.CLOSED);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void UpdateOrderState_WhenOrderIDInvalid_ExpectFalse(){
        IBotIO botIO = CreateIOObject();
        
        boolean result = botIO.UpdateOrderState(0, TradeState.CLOSED);
        
        // Assert
        assertFalse(result);
    }

    
    /*** Helper Methods ***/
    private IBotIO CreateIOObject() {
        IBotIO botIO = IOFactory.CreateIOObject("Test_DB");
        return botIO;
    }
    
    private IBotIO CreateBadIOObject() {
        IBotIO botIO = IOFactory.CreateIOObject(null);
        return botIO;
    }
    
    
}
