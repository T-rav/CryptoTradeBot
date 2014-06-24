/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.to;

import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class MarketBalanceList {
    
    private final HashMap<String, Double> _balances;
    
    public MarketBalanceList(){
        _balances = new HashMap<>();
    }
    
    public void AddBalance(String key, Double balance){
        _balances.put(key, balance);
    }
    
    public Double FetchBalance(String key){
        
        if(_balances.containsKey(key)){
            return _balances.get(key);
        }
        
        return -1.0;
    }
}
