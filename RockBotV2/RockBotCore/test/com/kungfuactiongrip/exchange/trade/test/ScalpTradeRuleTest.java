/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.trade.test;

import com.kungfuactiongrip.exchange.trade.TradeFactory;
import com.kungfuactiongrip.exchange.trade.TradeRule;
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
        TradeRule tr = TradeFactory.CreateScalpTradeRule();
        
        assertNotNull(tr);
    }

}
