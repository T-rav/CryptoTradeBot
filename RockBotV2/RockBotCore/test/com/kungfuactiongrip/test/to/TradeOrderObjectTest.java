/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test.to;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.to.TradeOrderFactory;
import com.kungfuactiongrip.to.TradeOrder;
import com.kungfuactiongrip.to.TradeType;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class TradeOrderObjectTest {
    
    public TradeOrderObjectTest() {
    }
    
    @Test
    public void CanCreateBuyOrder_ExpectValidObject(){
        TradeOrder bo = TradeOrderFactory.GenerateOrder(TradeType.BUY);
        
        assertEquals(bo.TradeType(), TradeType.BUY);
    }
    
    @Test
    public void CanCreateSellOrder_ExpectValidObject(){
        TradeOrder bo = TradeOrderFactory.GenerateOrder(TradeType.SELL);
        
        assertEquals(bo.TradeType(), TradeType.SELL);
    }
    
    @Test
    public void CanCreateBuyOrder_ExpectFieldsPopulated(){
        TradeOrder bo = TradeOrderFactory.GenerateOrder(TradeType.BUY);
        
        bo.SetPricePer(0.25);
        bo.SetTradeID("DUMMY-1");
        
        assertEquals(bo.TradeType(), TradeType.BUY);
        assertEquals(-1, bo.RowID);
        assertEquals(0.25, bo.PricePer, 0.0000001);
        assertEquals("DUMMY-1", bo.TradeID);
        
    }
    
     @Test
    public void CanCreateSellOrder_ExpectFieldsPopulated(){
        TradeOrder bo = TradeOrderFactory.GenerateOrder(TradeType.SELL);
        
        bo.SetPricePer(0.25);
        bo.SetTradeID("DUMMY-1");
        
        assertEquals(bo.TradeType(), TradeType.SELL);
        assertEquals(-1, bo.RowID);
        assertEquals(0.25, bo.PricePer, 0.0000001);
        assertEquals("DUMMY-1", bo.TradeID);
        
    }
    
    @Test
    public void CanCreateBuyOrderWithValues_ExpectFieldsPopulated(){
        TradeOrder bo = TradeOrderFactory.GenerateOrder(TradeType.BUY);
        
        bo.SetPricePer(0.25);
        bo.SetTradeID("DUMMY-1");
        
        assertEquals(bo.TradeType(), TradeType.BUY);
        assertEquals(-1, bo.RowID);
        assertEquals(0.25, bo.PricePer, 0.0000001);
        assertEquals("DUMMY-1", bo.TradeID);
        
    }
    
     @Test
    public void CanCreateSellOrderWithValues_ExpectFieldsPopulated(){
        TradeOrder bo = TradeOrderFactory.GenerateOrder(TradeType.SELL, -1, "DUMMY-1",0.1,null, ExchangeList.Cryptsy, "2014-01-01 01:02:03" );
        
        bo.SetPricePer(0.25);
        bo.SetTradeID("DUMMY-1");
        
        assertEquals(bo.TradeType(), TradeType.SELL);
        assertEquals(-1, bo.RowID);
        assertEquals(0.25, bo.PricePer, 0.0000001);
        assertEquals("DUMMY-1", bo.TradeID);
        
    }
    
}
