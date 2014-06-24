/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.trade;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.io.IBotIO;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author Administrator
 */
class TradeBotImpl implements ITradeBot {

    private final List<TradeRule> _rules;
    private final ExchangeList _exchange;
    private final IBotIO _dbObj;
    
    TradeBotImpl(ExchangeList exchange, IBotIO dbObj) {
        _rules = new ArrayList<>();
        _exchange = exchange;
        _dbObj = dbObj;
    }

    @Override
    public boolean AddTradeRule(TradeRule tr) {
        if(tr == null){
            return false;
        }
        
        _rules.add(tr);
        return true;
    }

    @Override
    public ExchangeList FetchActiveExchange() {
        return _exchange;
    }

    @Override
    public List<Integer> FetchActiveMarketList() {
        return _dbObj.FetchActiveMarketList(_exchange);
    }

    @Override
    public void RunTradeRules() {
        // TODO : Foreach rule/marketID pair create Thread to host process
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        // Here is where we log data from each trade rule's execution ;)
    }

}
