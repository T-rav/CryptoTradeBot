/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.trade;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.IExchange;
import com.kungfuactiongrip.exchange.TransactionType;
import com.kungfuactiongrip.exchange.io.IBotIO;
import com.kungfuactiongrip.exchange.to.MarketBuySellOrders;
import com.kungfuactiongrip.exchange.to.MarketCancelOrderResult;
import com.kungfuactiongrip.to.TradeOrder;
import com.kungfuactiongrip.to.TradeOrderFactory;
import com.kungfuactiongrip.to.TradeState;
import com.kungfuactiongrip.to.TradeType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author Administrator
 */
public class ScalpTradeRuleTest {
    
    public ScalpTradeRuleTest() {
    }

    @Test
    public void Ctor_ExpectRuleCreated(){
        TradeRule tr = CreateTradeRule();
        
        assertNotNull(tr);
    }

    private TradeRule CreateTradeRule() {
        TradeRule tr = TradeFactory.CreateScalpTradeRule();
        return tr;
    }
    
    @Test
    public void Run_WhenMarketIDSetToInvalidValue_ExpectFailsToRun(){
        TradeRule tr = CreateTradeRule();
        IBotIO dbObj = mock(IBotIO.class);
        IExchange exchangeObj = mock(IExchange.class); 
        
        
        Thread t = new Thread(tr);
        tr.setMarketID(0);
        tr.setDB(dbObj);
        tr.setExchange(exchangeObj);
        t.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(TradeBotTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        tr.shutdown();
        
        ExecuteState es = tr.fetchLastRunState();
        
        // Assert
        assertEquals(ExecuteState.RULE_RUN_ABORTED, es);
    }
    
    @Test
    public void Run_WhenDBObjectSetToNull_ExpectFailsToRun(){
        TradeRule tr = CreateTradeRule();
        IExchange exchangeObj = mock(IExchange.class); 
        
        Thread t = new Thread(tr);
        tr.setMarketID(1);
        tr.setDB(null);
        tr.setExchange(exchangeObj);
        t.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(TradeBotTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        tr.shutdown();
        
        ExecuteState es = tr.fetchLastRunState();
        
        // Assert
        assertEquals(ExecuteState.RULE_RUN_ABORTED, es);
    }
    
    @Test
    public void Run_WhenExchangeObjectSetToNull_ExpectFailsToRun(){
        TradeRule tr = CreateTradeRule();
        IBotIO dbObj = mock(IBotIO.class);
        
        Thread t = new Thread(tr);
        tr.setMarketID(1);
        tr.setDB(dbObj);
        tr.setExchange(null);
        t.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(TradeBotTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        tr.shutdown();
        
        ExecuteState es = tr.fetchLastRunState();
        
        // Assert
        assertEquals(ExecuteState.RULE_RUN_ABORTED, es);
    }
    
    @Test
    public void Run_WhenMarketIDSetToValidValue_ExpectRuleRuns(){
        TradeRule tr = CreateTradeRule();
        
        Thread t = new Thread(tr);
        tr.setMarketID(173);
        t.start();
        tr.shutdown();
        
        ExecuteState es = tr.fetchLastRunState();
        
        // Assert
        assertEquals(ExecuteState.NOP, es);
    }
    
    // ---- NOW THE FUN ;)
    
    private void SetupForBuy(IBotIO dbObj, IExchange exchangeObj, double bestBuy) throws Exception{
        SetupForBuy(dbObj, exchangeObj, bestBuy, null, null);
    }
    
    private void SetupForBuy(IBotIO dbObj, IExchange exchangeObj, double bestBuy, TradeOrder buyOrder) throws Exception{
        SetupForBuy(dbObj, exchangeObj, bestBuy, buyOrder, null);
    }
    
    private void SetupForBuy(IBotIO dbObj, IExchange exchangeObj, double bestBuy, TradeOrder buyOrder, TradeOrder sellOrder) throws Exception{
        // Setup
        List<TradeOrder> buyTrades = new ArrayList<>();
        List<TradeOrder> sellTrades = new ArrayList<>();
        MarketBuySellOrders orders = mock(MarketBuySellOrders.class);
        when(orders.FetchBestBuyPrice()).thenReturn(bestBuy);

        if(buyOrder != null){
            buyTrades.add(buyOrder);
        }
        
//         verify(dbObj).UpdateOrderState(Matchers.anyInt(), TradeState.ABORTED);
//            
//            verify(exchangeObj).FetchMarketOrders(Matchers.anyInt());
//            // Verify exchange was called to cancel the order ;)
//            verify(exchangeObj).CancelTrade(Matchers.anyString());
        
        // db setup
        when(dbObj.FetchOpenBuyOrdersForMarket(Matchers.anyInt())).thenReturn(buyTrades);
        when(dbObj.FetchOpenSellOrdersForMarket(Matchers.anyInt())).thenReturn(sellTrades);
        when(dbObj.UpdateOrderState(Matchers.anyInt(), Matchers.any(TradeState.class))).thenReturn(Boolean.TRUE);
        // exchange setup
        when(exchangeObj.FetchMarketOrders(Matchers.anyInt())).thenReturn(orders);
        when(exchangeObj.CancelTrade(Matchers.anyString())).thenReturn(new MarketCancelOrderResult("Message Here", false));
    }
    
    @Test
    public void Run_WhenOpenBuyOrderExist_WhenBuyIsNotBest_ExpectAbortedBuyOrder(){
        Thread t = null;
        try {
            TradeRule tr = CreateTradeRule();
            IBotIO dbObj = mock(IBotIO.class);
            IExchange exchangeObj = mock(IExchange.class);
            
            // Setup
            TradeOrder order = TradeOrderFactory.GenerateOrder(TradeType.BUY,1,"1",0.0011,null,ExchangeList.Cryptsy,"");
            SetupForBuy(dbObj, exchangeObj,0.001, order);
            
            // Run
            t = new Thread(tr);
            tr.setMarketID(1);
            tr.setDB(dbObj);
            tr.setExchange(exchangeObj);
            t.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(TradeBotTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            tr.shutdown();
            
            ExecuteState es = tr.fetchLastRunState();
            
            // Assert
            assertEquals(ExecuteState.ABORTED_BUY_ORDER, es);
            verify(dbObj, times(1)).FetchOpenBuyOrdersForMarket(Matchers.anyInt());
            verify(dbObj, times(1)).FetchOpenSellOrdersForMarket(Matchers.anyInt());
            // Verify the db was called to update its state
            verify(dbObj, times(1)).UpdateOrderState(Matchers.anyInt(), eq(TradeState.ABORTED));
            
            verify(exchangeObj, times(1)).FetchMarketOrders(Matchers.anyInt());
            // Verify exchange was called to cancel the order ;)
            verify(exchangeObj, times(1)).CancelTrade(Matchers.anyString());
        } catch (Exception ex) {
            assertTrue(false);
            Logger.getLogger(ScalpTradeRuleTest.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            t.stop();
        }
    }
    
    @Test
    public void Run_WhenOpenBuyOrderExist_WhenBuyIsBest_ExpectHoldBuyOrder(){
        Thread t = null;
        try {
            TradeRule tr = CreateTradeRule();
            IBotIO dbObj = mock(IBotIO.class);
            IExchange exchangeObj = mock(IExchange.class);
            
            // Setup
            TradeOrder order = TradeOrderFactory.GenerateOrder(TradeType.BUY,1,"1",0.001,null,ExchangeList.Cryptsy,"");
            SetupForBuy(dbObj, exchangeObj,0.001, order);
            
            // Run
            t = new Thread(tr);
            tr.setMarketID(1);
            tr.setDB(dbObj);
            tr.setExchange(exchangeObj);
            t.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(TradeBotTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            tr.shutdown();
            
            ExecuteState es = tr.fetchLastRunState();
            
            // Assert
            assertEquals(ExecuteState.HOLD_BUY_ORDER, es);
            verify(dbObj, times(1)).FetchOpenBuyOrdersForMarket(Matchers.anyInt());
            verify(dbObj,times(1)).FetchOpenSellOrdersForMarket(Matchers.anyInt());
            verify(dbObj, times(0)).UpdateOrderState(Matchers.anyInt(), Matchers.any(TradeState.class));
            verify(exchangeObj,times(1)).FetchMarketOrders(Matchers.anyInt());
            verify(exchangeObj, times(0)).CancelTrade(Matchers.anyString());
        } catch (Exception ex) {
            assertTrue(false);
            Logger.getLogger(ScalpTradeRuleTest.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            t.stop();
        }
    }
       
    @Test
    public void Run_WhenNoOpenBuyOrderExistAndMarginGoodEnough_ExpectCreateBuyOrder(){
        Thread t =null;
        try {
            TradeRule tr = CreateTradeRule();
            IBotIO dbObj = mock(IBotIO.class);
            IExchange exchangeObj = mock(IExchange.class);
            
            SetupForBuy(dbObj, exchangeObj, 0.001);
            
            // Run
            t = new Thread(tr);
            tr.setMarketID(1);
            tr.setDB(dbObj);
            tr.setExchange(exchangeObj);
            t.start();
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(TradeBotTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            tr.shutdown();
            
            ExecuteState es = tr.fetchLastRunState();
            
            // Assert
            assertEquals(ExecuteState.CREATED_BUY_ORDER, es);
            verify(dbObj,times(1)).FetchOpenBuyOrdersForMarket(Matchers.anyInt());
            verify(dbObj,times(1)).FetchOpenSellOrdersForMarket(Matchers.anyInt());
            // Verify exchange was called to create the order ;)
            verify(exchangeObj,times(1)).CreateTrade(Matchers.anyInt(), eq(TransactionType.Buy), Matchers.anyDouble(), Matchers.anyDouble());
            // Verify the insert happened ;)
            verify(dbObj,times(1)).InsertOrder(eq(TradeType.BUY),eq(TradeState.OPEN),Matchers.anyInt(), Matchers.anyDouble(), Matchers.anyDouble(), Matchers.anyString());
        } catch (Exception ex) {
            assertTrue(false);
            Logger.getLogger(ScalpTradeRuleTest.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            t.stop();
        }
    }
    
  
    
}
