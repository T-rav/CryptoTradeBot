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

/**
 *
 * @author Administrator
 */
class TradeBotImpl implements ITradeBot {

    private final List<TradeRule> _rules;
    private final ExchangeList _exchange;
    private final IBotIO _dbObj;
    private final List<Thread> _tradeRules;
    private final List<String> _botLog;
    
    TradeBotImpl(ExchangeList exchange, IBotIO dbObj) {
        _rules = new ArrayList<>();
        _botLog = new ArrayList<>();
        _tradeRules = new ArrayList<>();
        _exchange = exchange;
        _dbObj = dbObj;
    }
    
    @Override
    public void Shutdown(){
        
        // signal threads to exit
        for(TradeRule tr : _rules){
            tr.shutdown();
        }
        
        // wait thread to exit ;)
        for(Thread t : _tradeRules){
            if(t.isAlive()){
                try{
                    t.join(1000);
                }catch(InterruptedException e){}
            }
        }
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
        // for each rule
        for(TradeRule tr : _rules){
            // for each activem
            for(Integer i : FetchActiveMarketList()){
                tr.setMarketID(i);
                Thread t = new Thread(tr);
                _tradeRules.add(t);
                t.start();
            }
        }
    }
}
