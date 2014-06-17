/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io;

import com.kungfuactiongrip.to.BuyOrder;
import com.kungfuactiongrip.to.SellOrder;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface IBotIO {

    /**
     *
     * @param marketID
     * @return 
     */
    public List<BuyOrder> FetchOpenBuyOrdersForMarket(int marketID);

    public List<SellOrder> FetchOpenSellOrdersForMarket(int marketID);
    
}
