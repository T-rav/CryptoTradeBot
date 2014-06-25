/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.trade.test;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.trade.ITradeBot;
import com.kungfuactiongrip.exchange.trade.TradeFactory;
import com.kungfuactiongrip.exchange.trade.TradeRule;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Administrator
 */
public class TradeBotTest {
    
    public TradeBotTest() {
    }
    
    private ITradeBot MakeTradeBot() {
        ITradeBot tb = TradeFactory.CreateBot(ExchangeList.Cryptsy, "Test_DB");
        return tb;
    }
    
    @Test
    public void CreateBot_ExpectValidTradeBot(){
        ITradeBot tb = MakeTradeBot();
        
        assertNotNull(tb);
    }
    
    @Test
    public void CreateBot_WhenNullExchange_ExpectNullBot(){
        ITradeBot tb = TradeFactory.CreateBot(null, null);
        
        assertNull(tb);
    }
    
    @Test
    public void CreateBot_WhenNullPropertiesFile_ExpectNullBot(){
        ITradeBot tb = TradeFactory.CreateBot(ExchangeList.Cryptsy, null);
        
        assertNull(tb);
    }
    
    @Test
    public void CreateBot_WhenBadPropertiesFile_ExpectNullBot(){
        ITradeBot tb = TradeFactory.CreateBot(ExchangeList.Cryptsy, "FOOBAR");
        
        assertNull(tb);
    }
    
    @Test
    public void FetchActiveExchange_ExpectExchangeCreatedWithReturned(){
        ITradeBot tb = MakeTradeBot();
        
        ExchangeList exchange = tb.FetchActiveExchange();
        assertEquals(ExchangeList.Cryptsy, exchange);
    }
    
    @Test
    public void AddTradeRule_ExpectRuleAdded(){
        ITradeBot tb = MakeTradeBot();
        
        TradeRule tr = mock(TradeRule.class);
        boolean result = tb.AddTradeRule(tr);
        
        assertTrue(result);
    }
    
    @Test
    public void AddTradeRule_WhenNull_ExpectRuleNotAdded(){
        ITradeBot tb = MakeTradeBot();
        
        boolean result = tb.AddTradeRule(null);
        
        assertFalse(result);
    }
    
    @Test
    public void FetchActiveMarketList_WhenMarketsActive_ExpectNonEmptyList(){
        ITradeBot bot = MakeTradeBot();
        
        List<Integer> result = bot.FetchActiveMarketList();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void AddRunShutdownTradeRules_WhenRulesLoaded_ExpectRulesRunAndExit(){
        ITradeBot bot = MakeTradeBot();
        
        TradeRule tr = mock(TradeRule.class);
        
        bot.AddTradeRule(tr);
        bot.RunTradeRules();
        bot.Shutdown();
        
        // Assert - Verify Mocks did what they should ;)
        verify(tr).setMarketID(173);
        verify(tr).run();
        verify(tr).shutdown();
    }
}
