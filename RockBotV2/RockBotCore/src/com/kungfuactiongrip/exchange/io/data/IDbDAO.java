/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io.data;

import com.kungfuactiongrip.exchange.ExchangeList;
import com.kungfuactiongrip.exchange.io.IBotIO;
import com.kungfuactiongrip.to.TradeType;
import java.sql.Connection;

public interface IDbDAO extends IBotIO{
    
    public String FetchUser();
    
    public Connection CreateConnection();
    
        /**
     *
     * @param tradeType
     * @param exchange
     * @param marketID
     * @param hourInterval
     * @return
     */
    public int FetchNumberOfAbortedTradesForInterval(TradeType tradeType , ExchangeList exchange, int marketID, int hourInterval);
    
    /**
     *
     * @param tradeType
     * @param exchangeList
     * @param marketID
     * @return
     */
    public int FetchOpenOrderCount(TradeType tradeType, ExchangeList exchangeList, int marketID);

}
