/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.to;

import com.kungfuactiongrip.exchange.ExchangeList;

/**
 *
 * @author Administrator
 */
public class BuyOrder extends TradeOrder{

    BuyOrder(){
        super();
    }
    
    BuyOrder(int rowID, String tradeID, double pricePer, String linkedID, ExchangeList exchange, String rowTS) {
        super(rowID, tradeID, pricePer, linkedID, exchange, rowTS);
    }

    @Override
    public TradeType TradeType() {
        return TradeType.BUY;
    }

}
