/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io.data;

import com.kungfuactiongrip.config.exchange.PropertyBag;
import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.to.MarketTradeVerbose;
import com.kungfuactiongrip.to.TradeOrder;
import com.kungfuactiongrip.to.TradeState;
import com.kungfuactiongrip.to.TradeType;
import java.sql.Connection;
import java.util.List;

public interface IDbDAO {
    
    public String FetchUser();
    
    public Connection CreateConnection();
    
    /**
     *
     * @param tradeType
     * @param state
     * @param exchangeList
     * @param marketID
     * @return
     */
    public int FetchOrderCountOfType(TradeType tradeType, TradeState state, ExchangeList exchangeList, int marketID);
    
        
    /**
     *
     * @param tradeType
     * @param state
     * @param exchangeList
     * @param marketID
     * @param interval
     * @return
     */
    public int FetchOrderCountOfTypeForInterval(TradeType tradeType, TradeState state, ExchangeList exchangeList, int marketID, int interval);
    
     /**
     *
     * @param marketID
     * @param typeOf
     * @param state
     * @param exchange
     * @return 
     */
    public List<TradeOrder> FetchOrdersForMarket(int marketID, TradeType typeOf, TradeState state, ExchangeList exchange);

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
     * @param rowID
     * @param state
     * @return
     */
    public boolean UpdateOrderState(int rowID, TradeState state);
    
    /**
     *
     * @return
     */
    public PropertyBag FetchEngineConfiguration();
    
    /**
     *
     * @param trades
     * @param marketID
     * @param exchange
     * @return 
     */
    public boolean InsertTradeHistory(List<MarketTradeVerbose> trades, int marketID, ExchangeList exchange);

    /**
     *
     * @param exchange
     * @return
     */
    public List<Integer> FetchActiveMarketList(ExchangeList exchange);

}
