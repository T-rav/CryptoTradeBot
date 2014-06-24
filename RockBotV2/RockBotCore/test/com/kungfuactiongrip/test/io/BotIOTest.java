/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test.io;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.io.IBotIO;
import com.kungfuactiongrip.exchange.io.IOFactory;
import com.kungfuactiongrip.io.data.test.MySQLDBObjectTest;
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
public class BotIOTest {
    
    public BotIOTest() {
    }
    
    /*** Helper Methods ***/
    private IBotIO CreateIOObject() {
        IBotIO botIO = IOFactory.CreateIOObject("Test_DB", ExchangeList.Cryptsy);
        return botIO;
    }
    
    private IBotIO CreateBadIOObject() {
        IBotIO botIO = IOFactory.CreateIOObject(null, null);
        return botIO;
    }
    
    
    @Test
    public void CreateBotIOObject_WithPropertiesFile_ExpectValidObject(){
        IBotIO botIO = CreateIOObject();
        
        // Assert
        assertNotNull(botIO);
    }
    
    @Test
    public void CreateBotIOObject_WithNullPropertiesFile_ExpectValidObject(){
        IBotIO botIO = IOFactory.CreateIOObject(null, null);
        
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
        
        List<TradeOrder> orders = botIO.FetchOpenBuyOrdersForMarket(MySQLDBObjectTest.InvalidOrderMarketID);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
        @Test
    public void FetchOpenBuyOrders_WithMarketIDWhereNoOrderAndInvalidDB_ExpectNoOrders(){
        IBotIO botIO = CreateBadIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenBuyOrdersForMarket(MySQLDBObjectTest.InvalidOrderMarketID);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void FetchOpenSellOrders_WithMarketIDWhereNoOrder_ExpectNoOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenSellOrdersForMarket(MySQLDBObjectTest.InvalidOrderMarketID);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void FetchOpenSellOrders_WithMarketIDWhereNoOrderAndInvalidDB_ExpectNoOrders(){
        IBotIO botIO = CreateBadIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenSellOrdersForMarket(MySQLDBObjectTest.InvalidOrderMarketID);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void FetchOpenBuyOrders_WithMarketIDWhereOrder_ExpectOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenBuyOrdersForMarket(MySQLDBObjectTest.ValidOrderMarketID);
        
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
        
        List<TradeOrder> orders = botIO.FetchOpenBuyOrdersForMarket(MySQLDBObjectTest.ValidOrderMarketID);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void FetchOpenSellOrders_WithMarketIDWhereOrder_ExpectOrders(){
        IBotIO botIO = CreateIOObject();
        
        List<TradeOrder> orders = botIO.FetchOpenSellOrdersForMarket(MySQLDBObjectTest.ValidOrderMarketID);
        
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
        
        List<TradeOrder> orders = botIO.FetchOpenSellOrdersForMarket(MySQLDBObjectTest.ValidOrderMarketID);
        
        // Assert
        assertTrue(orders.isEmpty());
    }
    
    @Test
    public void InsertOrder_WhenLinkedID_ExpectValidRowID(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.InsertOrder(TradeType.BUY, TradeState.OPEN, MySQLDBObjectTest.InsertOrderMarketID, 0.01, 0.1, "DUMMY-INSERT", "DUMMY-LINK");
        
        // Assert
         assertTrue(result > 0);
    }
    
    @Test
    public void InsertOrder_WhenNullDBObjectWithLinkedTradeID_ExpectBadRowId(){
        IBotIO botIO = CreateBadIOObject();
        
        int result = botIO.InsertOrder(TradeType.BUY, TradeState.OPEN, MySQLDBObjectTest.InsertOrderMarketID, 0.01, 0.1, "DUMMY-INSERT", "DUMMY-LINK");
        
        // Assert
         assertTrue(result < 0);
    }
    
    @Test
    public void InsertOrder_WhenNoLinkedID_ExpectValidRowID(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.InsertOrder(TradeType.BUY, TradeState.OPEN,MySQLDBObjectTest.InsertOrderMarketID, 0.01, 0.1, "DUMMY-INSERT");
        
        // Assert
         assertTrue(result > 0);
    }
    
    @Test
    public void InsertOrder_WhenNullDBObjectWithNoLinkedTradeID_ExpectBadRowId(){
        IBotIO botIO = CreateBadIOObject();
        
        int result = botIO.InsertOrder(TradeType.BUY, TradeState.OPEN, MySQLDBObjectTest.InsertOrderMarketID, 0.01, 0.1, "DUMMY-INSERT");
        
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
    
    @Test
    public void FetchNumberOfOpenBuyOrdersForMarketForInterval_WhenOrderIDValid_ExpectOneOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfOpenBuyOrdersForMarketForInterval(MySQLDBObjectTest.OpenIntervalMarketID,6);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void FetchNumberOfOpenBuyOrdersForMarketForInterval_WhenOrderIDInvalid_ExpectNoOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfOpenBuyOrdersForMarketForInterval((MySQLDBObjectTest.OpenIntervalMarketID-1),6);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void FetchNumberOfOpenSellOrdersForMarketForInterval_WhenOrderIDValid_ExpectOneOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfOpenSellOrdersForMarketForInterval(MySQLDBObjectTest.OpenIntervalMarketID,6);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void FetchNumberOfOpenSellOrdersForMarketForInterval_WhenOrderIDInvalid_ExpectNoOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfOpenSellOrdersForMarketForInterval((MySQLDBObjectTest.OpenIntervalMarketID-1),6);
        
        // Assert
        assertEquals(0, result);
    }
    
    // ------ OPEN TEST ------
    @Test
    public void FetchNumberOfOpenBuyOrdersForMarketForDay_WhenOrderIDValid_ExpectThreeOrders(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfOpenBuyOrdersForMarketForDay(MySQLDBObjectTest.OpenDayMarketID);
        
        // Assert
        assertEquals(3, result);
    }
    
    @Test
    public void FetchNumberOfOpenBuyOrdersForMarketForDay_WhenOrderIDInvalid_ExpectNoOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfOpenBuyOrdersForMarketForDay((MySQLDBObjectTest.OpenDayMarketID-1));
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void FetchNumberOfOpenSellOrdersForMarketForDay_WhenOrderIDValid_ExpectThreeOrders(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfOpenSellOrdersForMarketForDay(MySQLDBObjectTest.OpenDayMarketID);
        
        // Assert
        assertEquals(3, result);
    }
    
    @Test
    public void FetchNumberOfOpenSellOrdersForMarketForDay_WhenOrderIDInvalid_ExpectNoOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfOpenSellOrdersForMarketForDay((MySQLDBObjectTest.OpenIntervalMarketID-1));
        
        // Assert
        assertEquals(0, result);
    }

    // --- ABORTED TEST ---
        
    @Test
    public void FetchNumberOfAbortedBuyOrdersForInterval_WhenOrderIDValid_ExpectOneOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfAbortedBuyOrdersForInterval(MySQLDBObjectTest.AbortedIntervalMarketID,6);
        
        // Assert
        assertEquals(1, result);
    }
    
    
    @Test
    public void FetchNumberOfAbortedBuyOrdersForInterval_WhenOrderIDInvalid_ExpectOneOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfAbortedBuyOrdersForInterval((MySQLDBObjectTest.AbortedIntervalMarketID-1),6);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void FetchNumberOfAbortedSellOrdersForInterval_WhenOrderIDValid_ExpectOneOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfAbortedSellOrdersForInterval(MySQLDBObjectTest.AbortedIntervalMarketID,6);
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void FetchNumberOfAbortedSellOrdersForInterval_WhenOrderIDInvalid_ExpectOneOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfAbortedBuyOrdersForInterval((MySQLDBObjectTest.AbortedIntervalMarketID-1),6);
        
        // Assert
        assertEquals(0, result);
    }
    
    // ----
    
    @Test
    public void FetchNumberOfAbortedBuyOrdersForDay_WhenOrderIDValid_ExpectTwoOrders(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfAbortedBuyOrdersForDay(MySQLDBObjectTest.AbortedDayMarketID);
        
        // Assert
        assertEquals(2, result);
    }
    
    
    @Test
    public void FetchNumberOfAbortedBuyOrdersForDay_WhenOrderIDInvalid_ExpectNoOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfAbortedBuyOrdersForDay((MySQLDBObjectTest.AbortedDayMarketID-1));
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void FetchNumberOfAbortedSellOrdersForDay_WhenOrderIDValid_ExpectTwoOrders(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfAbortedSellOrdersForDay(MySQLDBObjectTest.AbortedDayMarketID);
        
        // Assert
        assertEquals(2, result);
    }
    
    @Test
    public void FetchNumberOfAbortedSellOrdersForDay_WhenOrderIDInvalid_ExpectNoOrder(){
        IBotIO botIO = CreateIOObject();
        
        int result = botIO.FetchNumberOfAbortedBuyOrdersForDay((MySQLDBObjectTest.AbortedDayMarketID-1));
        
        // Assert
        assertEquals(0, result);
    }

    @Test
    public void FetchActiveMarketList_WhenValidDBObject_ExpectNonEmptyList(){
        IBotIO botIO = CreateIOObject();
        
        List<Integer> result = botIO.FetchActiveMarketList(ExchangeList.Cryptsy);
        
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void FetchActiveMarketList_WhenInvalidDBObject_ExpectNonEmptyList(){
        IBotIO botIO = CreateBadIOObject();
        
        List<Integer> result = botIO.FetchActiveMarketList(ExchangeList.Cryptsy);
        
        assertTrue(result.isEmpty());
    }
    
}
