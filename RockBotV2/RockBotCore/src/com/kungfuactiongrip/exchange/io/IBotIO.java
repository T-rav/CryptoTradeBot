/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.to.TradeOrder;
import com.kungfuactiongrip.to.TradeState;
import com.kungfuactiongrip.to.TradeType;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface IBotIO {

    /**
     *
     * @return
     */
    public String FetchUser();
    
    /**
     *
     * @param marketID
     * @param exchange
     * @return 
     */
    public List<TradeOrder> FetchOpenBuyOrdersForMarket(int marketID, ExchangeList exchange);

    /**
     *
     * @param marketID
     * @param exchange
     * @return
     */
    public List<TradeOrder> FetchOpenSellOrdersForMarket(int marketID, ExchangeList exchange);
    
    /**
     *
     * @param type
     * @param state
     * @param exchange
     * @param marketID
     * @param pricePer
     * @param totalValue
     * @param tradeID
     * @param linkedID
     * @return
     */
    public int InsertOrder(TradeType type, TradeState state,ExchangeList exchange, int marketID, double pricePer, double totalValue, String tradeID, String linkedID);
    
    /**
     *
     * @param type
     * @param state
     * @param exchange
     * @param marketID
     * @param pricePer
     * @param totalValue
     * @param tradeID
     * @return
     */
    public int InsertOrder(TradeType type, TradeState state,ExchangeList exchange, int marketID, double pricePer, double totalValue, String tradeID);
 
    /**
     *
     * @param rowID
     * @param state
     * @return
     */
    public boolean UpdateOrderState(int rowID, TradeState state);
    
    /**
     *
     * @param marketID
     * @param exchange
     * @param hourInterval
     * @return
     */
    public int FetchNumberOfOpenBuyOrdersForMarketForInterval(int marketID, ExchangeList exchange, int hourInterval);
    
    /**
     *
     * @param marketID
     * @param exchange
     * @param hourInterval
     * @return
     */
    public int FetchNumberOfOpenSellOrdersForMarketForInterval(int marketID, ExchangeList exchange, int hourInterval);
    
    /**
     *
     * @param marketID
     * @param exchange
     * @return
     */
    public int FetchNumberOfOpenBuyOrdersForMarketForDay(int marketID, ExchangeList exchange);
    
    /**
     *
     * @param marketID
     * @param exchange
     * @return
     */
    public int FetchNumberOfOpenSellOrdersForMarketForDay(int marketID, ExchangeList exchange);
    
    
    /**
     *
     * @param marketID
     * @param exchange
     * @param hourInterval
     * @return
     */
    public int FetchNumberOfAbortedSellOrdersForInterval(int marketID, ExchangeList exchange, int hourInterval);
    
    /**
     *
     * @param marketID
     * @param exchange
     * @param hourInterval
     * @return
     */
    public int FetchNumberOfAbortedBuyOrdersForInterval(int marketID, ExchangeList exchange, int hourInterval);
    
    /**
     *
     * @param marketID
     * @param exchange
     * @return
     */
    public int FetchNumberOfAbortedSellOrdersForDay(int marketID, ExchangeList exchange);
    
    /**
     *
     * @param marketID
     * @param exchange
     * @return
     */
    public int FetchNumberOfAbortedBuyOrdersForDay(int marketID, ExchangeList exchange);
   
}
