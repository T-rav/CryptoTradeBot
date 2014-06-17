/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.to.BuyOrder;
import com.kungfuactiongrip.to.SellOrder;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class MySQLDBObjectTest {
    
    public MySQLDBObjectTest() {
    }


    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void CanCreateObjectWithoutPropertyFile_ExpectValidObject(){
        MySQLDBObject obj = new MySQLDBObject();
        
        assertNotNull(obj);
        assertEquals("username", obj.FetchUser());
    }
    
    @Test
    public void CanCreateObjectWithPropertyFile_ExpectValidObject(){
        MySQLDBObject obj = new MySQLDBObject("Test_DB");
        
        assertNotNull(obj);
        assertEquals("tradebot", obj.FetchUser());
    }
    
    @Test
    public void CanCreateObjectWithNullPropertyFile_ExpectDefaultObject(){
        MySQLDBObject obj = new MySQLDBObject(null);
        
        assertNotNull(obj);
        assertEquals("username", obj.FetchUser());
    }
    
    @Test
    public void CanFetchOpenBuyOrdersForMarket_WhenNoOrdersExsit_ExpectEmptyList(){
        MySQLDBObject obj = new MySQLDBObject(null);
        
        assertNotNull(obj);
        List<BuyOrder> order = obj.FetchOpenBuyOrdersForMarket(173);
        assertTrue(order.isEmpty());
    }
    
    @Test
    public void CanFetchOpenBuySellOrdersForMarket_WhenNoOrdersExsit_ExpectEmptyList(){
        MySQLDBObject obj = new MySQLDBObject(null);
        
        assertNotNull(obj);
        List<SellOrder> order = obj.FetchOpenSellOrdersForMarket(173);
        assertTrue(order.isEmpty());
    }
    
    @Test
    public void CanFetchOpenBuyOrdersForMarket_WhenOrdersExsit_ExpectFiveItemList(){
        MySQLDBObject obj = new MySQLDBObject(null);
        
        assertNotNull(obj);
        List<BuyOrder> order = obj.FetchOpenBuyOrdersForMarket(173);
        assertFalse(order.isEmpty());
        assertEquals(5,order.size());
    }
    
    @Test
    public void CanFetchOpenBuySellOrdersForMarket_WhenOrdersExsit_ExpectFiveItemList(){
        MySQLDBObject obj = new MySQLDBObject(null);
        
        assertNotNull(obj);
        List<SellOrder> order = obj.FetchOpenSellOrdersForMarket(173);
        assertFalse(order.isEmpty());
        assertEquals(5,order.size());
    }
    
    
}
