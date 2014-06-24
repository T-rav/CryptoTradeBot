/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.to;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Administrator
 */
public class MarketTradeVerbose extends MarketTrade {
    
    @SerializedName("tradetype")
    public String TransactedTradeType;
    
    @SerializedName("fee")
    public Double Fee;
    
    @SerializedName("order_id")
    public String OrderID;
    
}
