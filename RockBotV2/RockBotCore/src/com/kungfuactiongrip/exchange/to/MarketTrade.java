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
public class MarketTrade {
    
    @SerializedName("tradeid")
    public String TradeID;
    
    @SerializedName("datetime")
    public String DateTime;
    
    @SerializedName("tradeprice")
    public double TradePrice;
    
    @SerializedName("quantity")
    public double TradeQty;
    
    @SerializedName("total")
    public double Total;
    
    @SerializedName("initiate_ordertype")
    public String OrderType;
}
