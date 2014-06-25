/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.trade;

import com.kungfuactiongrip.exchange.ExchangeList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface ITradeBot {

    public boolean AddTradeRule(TradeRule tr);

    public ExchangeList FetchActiveExchange();
    
    public List<Integer> FetchActiveMarketList();
    
    public void RunTradeRules();
    
    public void Shutdown();

    public double FetchPerTradeAmount();
    
}
