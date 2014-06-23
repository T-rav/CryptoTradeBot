/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.exchange.objects.MarketBuySellOrders;
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
     * @return 
     */
    public List<TradeOrder> FetchOpenBuyOrdersForMarket(int marketID);

    /**
     *
     * @param marketID
     * @return
     */
    public List<TradeOrder> FetchOpenSellOrdersForMarket(int marketID);
    
    /**
     *
     * @param type
     * @param state
     * @param marketID
     * @param pricePer
     * @param totalValue
     * @param tradeID
     * @param linkedID
     * @return
     */
    public int InsertOrder(TradeType type, TradeState state, int marketID, double pricePer, double totalValue, String tradeID, String linkedID);
    
    /**
     *
     * @param type
     * @param state
     * @param marketID
     * @param pricePer
     * @param totalValue
     * @param tradeID
     * @return
     */
    public int InsertOrder(TradeType type, TradeState state,int marketID, double pricePer, double totalValue, String tradeID);
 
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
     * @param hourInterval
     * @return
     */
    public int FetchNumberOfOpenBuyOrdersForMarketForInterval(int marketID, int hourInterval);
    
    /**
     *
     * @param marketID
     * @param hourInterval
     * @return
     */
    public int FetchNumberOfOpenSellOrdersForMarketForInterval(int marketID, int hourInterval);
    
    /**
     *
     * @param marketID
     * @return
     */
    public int FetchNumberOfOpenBuyOrdersForMarketForDay(int marketID);
    
    /**
     *
     * @param marketID
     * @return
     */
    public int FetchNumberOfOpenSellOrdersForMarketForDay(int marketID);
    
    
    /**
     *
     * @param marketID
     * @param hourInterval
     * @return
     */
    public int FetchNumberOfAbortedSellOrdersForInterval(int marketID, int hourInterval);
    
    /**
     *
     * @param marketID
     * @param hourInterval
     * @return
     */
    public int FetchNumberOfAbortedBuyOrdersForInterval(int marketID, int hourInterval);
    
    /**
     *
     * @param marketID
     * @return
     */
    public int FetchNumberOfAbortedSellOrdersForDay(int marketID);
    
    /**
     *
     * @param marketID
     * @return
     */
    public int FetchNumberOfAbortedBuyOrdersForDay(int marketID);
    
    // -- Test From Here Down
    
    /**
     *
     * @param marketID
     * @return
     */
    public MarketBuySellOrders FetchMarketOrders(int marketID);
    
//    /**
//     *
//     * @param marketID
//     * @return 
//     */
//    public List<MarketTrade> FetchMarketTrades(int marketID);

    
   
}
