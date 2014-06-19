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
    public boolean InsertOrder(TradeType type, TradeState state,ExchangeList exchange, int marketID, double pricePer, double totalValue, String tradeID, String linkedID);
 
    /**
     *
     * @param rowID
     * @param state
     * @return
     */
    public boolean UpdateOrderState(int rowID, TradeState state);
}
