/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.test.to;

import com.kungfuactiongrip.exchange.to.MarketBalanceList;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class MarketBalanceListTest {
    
    public MarketBalanceListTest() {
    }
    
    @Test
    public void Ctor_ExpectNotNull(){
        MarketBalanceList mbl = new MarketBalanceList();
        
        assertNotNull(mbl);
    }
    
    @Test
    public void AddBalance_WhenNormalOperation_ExpectAdded(){
        MarketBalanceList mbl = new MarketBalanceList();
        mbl.AddBalance("BTC", 0.1);
    }
    
    @Test
    public void FetchBalance_WhenKeyExist_ExpectBalance(){
        MarketBalanceList mbl = new MarketBalanceList();
        mbl.AddBalance("BTC", 0.1);
        Double balance = mbl.FetchBalance("BTC");
        
        assertEquals(0.1, balance, 0.0000000001);
    }
    
    @Test
    public void FetchBalance_WhenKeyDoesNotExist_ExpectNegativeOne(){
        MarketBalanceList mbl = new MarketBalanceList();
        mbl.AddBalance("BTC", 0.1);
        Double balance = mbl.FetchBalance("BT");
        
        assertEquals(-1.0, balance, 0.0000000001);
    }
}
