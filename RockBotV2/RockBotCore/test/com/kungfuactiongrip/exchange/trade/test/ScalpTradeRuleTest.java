/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.trade.test;

import com.kungfuactiongrip.exchange.trade.ExecuteState;
import com.kungfuactiongrip.exchange.trade.TradeFactory;
import com.kungfuactiongrip.exchange.trade.TradeRule;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

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
    public void Run_WhenMarketIDSetToValidValue_ExpectRuleRuns(){
        TradeRule tr = CreateTradeRule();
        
        Thread t = new Thread(tr);
        tr.setMarketID(173);
        t.start();
        tr.shutdown();
        
        ExecuteState es = tr.fetchLastRunState();
        
        assertEquals(ExecuteState.NOP, es);
    }
    
    @Test
    public void Run_WhenMarketIDSetToInvalidValue_ExpectRuleRuns(){
        TradeRule tr = CreateTradeRule();
        
        Thread t = new Thread(tr);
        tr.setMarketID(0);
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

}
