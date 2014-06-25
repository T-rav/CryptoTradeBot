/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.trade;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.IExchange;
import com.kungfuactiongrip.exchange.IExchangeGenerator;
import com.kungfuactiongrip.exchange.io.IBotIO;
import com.kungfuactiongrip.exchange.to.MarketBalanceList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// http://docs.mockito.googlecode.com/hg/latest/org/mockito/Mockito.html#3

public class TradeBotTest {
    
    public TradeBotTest() {
    }
    
    // ---- HELPERS
    
    private ITradeBot MakeTradeBot(double initBalance) {
        try {
            IExchangeGenerator exchange = mock(IExchangeGenerator.class);
            IExchange exchangeObj = MakeExchangeObject(initBalance);
            when(exchange.GenerateExchangeObject()).thenReturn(exchangeObj);
            
            ITradeBot tb = TradeFactory.CreateBot(exchange, ExchangeList.Cryptsy, "Test_DB");
            return tb;
        } catch (Exception ex) {
            Logger.getLogger(TradeBotTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private IExchange MakeExchangeObject(double balance) throws Exception {
        IExchange exchangeObj = mock(IExchange.class);
        
        // Setup Market Balances ;)
        MarketBalanceList mbl = mock(MarketBalanceList.class);
        when(mbl.FetchBalance("BTC")).thenReturn(balance);
        when(exchangeObj.FetchBalances()).thenReturn(mbl);
        
        return exchangeObj;
    }

    private IBotIO MakeDBObject() {
        // Setup
        IBotIO dbObj = mock(IBotIO.class);
        
        
        return dbObj;
    }
    
// ---- END HELPERS
    
    @Test
    public void CreateBot_ExpectValidTradeBot(){
        ITradeBot tb = MakeTradeBot(0.1);
        
        // Assert
        assertNotNull(tb);
    }
    
    @Test
    public void CreateBot_WhenNullExchange_ExpectNullBot(){
        IExchangeGenerator exchange = mock(IExchangeGenerator.class);
        ITradeBot tb = TradeFactory.CreateBot(exchange, null, "TEST_DB");
        
        // Assert
        assertNull(tb);
    }
    
    @Test
    public void CreateBot_WhenNullExchangeGenerator_ExpectNullBot(){
        ITradeBot tb = TradeFactory.CreateBot(null, ExchangeList.Cryptsy, "TEST_DB");
        
        // Assert
        assertNull(tb);
    }
    
    @Test
    public void CreateBot_WhenNullPropertiesFile_ExpectNullBot(){
        IExchangeGenerator exchange = mock(IExchangeGenerator.class);
        ITradeBot tb = TradeFactory.CreateBot(exchange, ExchangeList.Cryptsy, null);
        
        // Assert
        assertNull(tb);
    }
    
    @Test
    public void CreateBot_WhenBadPropertiesFile_ExpectNullBot(){
        IExchangeGenerator exchange = mock(IExchangeGenerator.class);
        ITradeBot tb = TradeFactory.CreateBot(exchange, ExchangeList.Cryptsy, "FOOBAR");
        
        // Assert
        assertNull(tb);
    }
    
    @Test
    public void FetchActiveExchange_ExpectExchangeCreatedWithReturned(){
        ITradeBot tb = MakeTradeBot(0.1);
        
        ExchangeList exchange = tb.FetchActiveExchange();
        
        // Assert
        assertEquals(ExchangeList.Cryptsy, exchange);
    }
    
    @Test
    public void AddTradeRule_ExpectRuleAdded(){
        ITradeBot tb = MakeTradeBot(0.1);
        
        TradeRule tr = mock(TradeRule.class);
        boolean result = tb.AddTradeRule(tr);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void AddTradeRule_WhenNull_ExpectRuleNotAdded(){
        ITradeBot tb = MakeTradeBot(0.1);
        
        boolean result = tb.AddTradeRule(null);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void FetchActiveMarketList_WhenMarketsActive_ExpectNonEmptyList(){
        ITradeBot bot = MakeTradeBot(0.1);
        
        List<Integer> result = bot.FetchActiveMarketList();
        
        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
       
    @Test
    public void CalcualateTradeAmount_Expect(){
        try {
            ITradeBot bot = MakeTradeBot(0.1);
            
            // Execution
            double perTradeAmount = bot.FetchPerTradeAmount();
            
            // Assert
            assertEquals(0.002, perTradeAmount,0.000000001);
        } catch (Exception ex) {
            assertTrue(false);
            Logger.getLogger(TradeBotTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void AddRunShutdownTradeRules_WhenRulesLoaded_ExpectRulesRunAndExit(){
        ITradeBot bot = MakeTradeBot(0.1);
        
        // Setup
        TradeRule tr = mock(TradeRule.class);
        
        // Execution
        bot.AddTradeRule(tr);
        bot.RunTradeRules();
        bot.Shutdown();
        
        // Assert - Verify Mocks did what they should ;)
        verify(tr).setMarketID(Matchers.anyInt());
        verify(tr).setDB(Matchers.any(IBotIO.class));
        verify(tr).setExchange(Matchers.any(IExchange.class));
        verify(tr).run();
        verify(tr).shutdown();
    }
 
}
