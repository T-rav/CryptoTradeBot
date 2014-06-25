/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.trade;

import com.kungfuactiongrip.exchange.IExchange;
import com.kungfuactiongrip.exchange.io.IBotIO;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;

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
        
        assertEquals(ExecuteState.NOP, es);
    }
    
}
