/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.trade;

import com.kungfuactiongrip.config.exchange.PropertyBag;
import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.IExchange;
import com.kungfuactiongrip.exchange.IExchangeGenerator;
import com.kungfuactiongrip.exchange.io.IBotIO;
import com.kungfuactiongrip.exchange.to.MarketBalanceList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
class TradeBotImpl implements ITradeBot {

    private final List<TradeRule> _rules;
    private final ExchangeList _exchange;
    private final IExchange _exchangeObj;
    private final IBotIO _dbObj;
    private final List<Thread> _tradeRules;
    
    TradeBotImpl(IExchangeGenerator exchangeGenerator, ExchangeList exchange, IBotIO dbObj) {
        _rules = new ArrayList<>();
        _tradeRules = new ArrayList<>();
        _exchange = exchange;
        _exchangeObj = exchangeGenerator.GenerateExchangeObject();
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
                
                // set key values for rule ;)
                tr.setMarketID(i);
                tr.setDB(_dbObj);
                tr.setExchange(_exchange.GenerateExchangeObject());
                
                // run the rule ;)
                Thread t = new Thread(tr);
                _tradeRules.add(t);
                t.start();
            }
        }
    }

    @Override
    public double FetchPerTradeAmount() {
        // fetch from DB
        // fetch amount from exchange
        MarketBalanceList mbl;
        double result = 0.0;
        try {
            
            mbl = _exchangeObj.FetchBalances();
            double currentBalance = mbl.FetchBalance(TradeConstants.BitcoinKey);
            PropertyBag properties = _dbObj.FetchEngineConfiguration();
            String value = properties.FetchKey(TradeConstants.MAX_TRADE_PERCENT_OF_TOTAL);
            double percentAmount = Double.parseDouble(value);
            result =  (currentBalance * percentAmount);
            
            if(result < _exchangeObj.FetchTradeMinimum()){
                result = 0.0;
            }
            
        } catch (Exception ex) {
            Logger.getLogger(TradeBotImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
